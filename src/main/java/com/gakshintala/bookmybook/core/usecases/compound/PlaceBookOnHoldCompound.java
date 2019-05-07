package com.gakshintala.bookmybook.core.usecases.compound;

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceId;
import com.gakshintala.bookmybook.core.domain.patron.HoldDuration;
import com.gakshintala.bookmybook.core.domain.patron.Patron;
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent;
import com.gakshintala.bookmybook.core.ports.UseCase;
import com.gakshintala.bookmybook.core.usecases.compound.AddBookEverywhere.AddBookEverywhereCommand;
import com.gakshintala.bookmybook.core.usecases.patron.CreatePatron;
import com.gakshintala.bookmybook.core.usecases.patron.CreatePatron.CreatePatronCommand;
import com.gakshintala.bookmybook.core.usecases.patron.PatronPlaceBookOnHold;
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
                .map(tuple2 -> createPatron.execute(command.createPatronCommand)
                        .map(patron -> patron.canPatronPlaceOnHold(tuple2._2, command.holdDuration))
                        .flatMap(placeBookOnHold::handleResult))
                .get();
    }

    @Value
    public static class PlaceOnHoldCompoundCommand {
        @NonNull AddBookEverywhereCommand addBookEverywhereCommand;
        @NonNull CreatePatronCommand createPatronCommand;
        @NonNull Instant timestamp;
        @NonNull HoldDuration holdDuration;
    }

}
