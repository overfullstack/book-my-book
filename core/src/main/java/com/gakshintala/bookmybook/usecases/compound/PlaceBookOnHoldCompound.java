package com.gakshintala.bookmybook.usecases.compound;


import com.gakshintala.bookmybook.domain.catalogue.CatalogueBookInstanceId;
import com.gakshintala.bookmybook.domain.patron.HoldDuration;
import com.gakshintala.bookmybook.domain.patron.Patron;
import com.gakshintala.bookmybook.domain.patron.PatronEvent;
import com.gakshintala.bookmybook.ports.UseCase;
import com.gakshintala.bookmybook.usecases.compound.AddBookEverywhere.AddBookEverywhereCommand;
import com.gakshintala.bookmybook.usecases.patron.CreatePatron;
import com.gakshintala.bookmybook.usecases.patron.CreatePatron.CreatePatronCommand;
import com.gakshintala.bookmybook.usecases.patron.PatronPlaceBookOnHold;
import io.vavr.Tuple3;
import io.vavr.control.Try;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PlaceBookOnHoldCompound implements UseCase<PlaceBookOnHoldCompound.PlaceOnHoldCompoundCommand, Try<Tuple3<PatronEvent, Patron, CatalogueBookInstanceId>>> {
    private final AddBookEverywhere addBookEverywhere;
    private final CreatePatron createPatron;
    private final PatronPlaceBookOnHold placeBookOnHold;

    @Override
    public Try<Tuple3<PatronEvent, Patron, CatalogueBookInstanceId>> execute(@NonNull PlaceBookOnHoldCompound.PlaceOnHoldCompoundCommand command) {
        return addBookEverywhere.execute(command.getAddBookEverywhereCommand())
                .flatMap(tuple2 -> createPatron.execute(command.createPatronCommand)
                        .map(patron -> patron.canPatronPlaceOnHold(tuple2._2, command.holdDuration))
                        .flatMap(placeBookOnHold::handleResult));
    }

    @Value
    public static class PlaceOnHoldCompoundCommand {
        @NonNull AddBookEverywhereCommand addBookEverywhereCommand;
        @NonNull CreatePatronCommand createPatronCommand;
        @NonNull Instant timestamp;
        @NonNull HoldDuration holdDuration;
    }

}
