package com.gakshintala.bookmybook.restexchange.compound.request;


import com.gakshintala.bookmybook.domain.catalogue.BookType;
import com.gakshintala.bookmybook.domain.catalogue.CatalogueBook;
import com.gakshintala.bookmybook.domain.patron.HoldDuration;
import com.gakshintala.bookmybook.domain.patron.PatronId;
import com.gakshintala.bookmybook.domain.patron.PatronInformation;
import com.gakshintala.bookmybook.domain.patron.PatronType;
import com.gakshintala.bookmybook.restexchange.patron.request.CreatePatronRequest;
import com.gakshintala.bookmybook.usecases.compound.AddBookEverywhere.AddBookEverywhereCommand;
import com.gakshintala.bookmybook.usecases.compound.PlaceBookOnHoldCompound.PlaceOnHoldCompoundCommand;
import com.gakshintala.bookmybook.usecases.patron.CreatePatron.CreatePatronCommand;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Value
public class PlaceBookOnHoldCompoundRequest {
    @NotNull
    AddBookEverywhereRequest addBookEverywhere;
    @NotNull
    CreatePatronRequest patron;
    Integer noOfDays;

    public PlaceOnHoldCompoundCommand toCommand() {
        return new PlaceOnHoldCompoundCommand(
                new AddBookEverywhereCommand(
                        new CatalogueBook(
                                addBookEverywhere.getBookInfo().getBookIsbn(),
                                addBookEverywhere.getBookInfo().getTitle(),
                                addBookEverywhere.getBookInfo().getAuthor()
                        ), BookType.fromString(addBookEverywhere.getBookType())
                ),
                new CreatePatronCommand(
                        new PatronInformation(
                                PatronId.anyPatronId(),
                                PatronType.fromString(patron.getPatronType())
                        )
                ),
                Instant.now(),
                HoldDuration.forNoOfDays(noOfDays)
        );
    }

}
