package com.gakshintala.bookmybook.adapters.rest.compound.request;

import com.gakshintala.bookmybook.adapters.rest.patron.request.CreatePatronRequest;
import com.gakshintala.bookmybook.core.domain.catalogue.BookType;
import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBook;
import com.gakshintala.bookmybook.core.domain.patron.HoldDuration;
import com.gakshintala.bookmybook.core.domain.patron.PatronId;
import com.gakshintala.bookmybook.core.domain.patron.PatronInformation;
import com.gakshintala.bookmybook.core.domain.patron.PatronType;
import com.gakshintala.bookmybook.core.usecases.compound.PlaceBookOnHoldCompound.PlaceOnHoldCompoundCommand;
import com.gakshintala.bookmybook.core.usecases.compound.AddBookEverywhere.AddBookEverywhereCommand;
import com.gakshintala.bookmybook.core.usecases.patron.CreatePatron.CreatePatronCommand;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Value
public class PlaceBookOnHoldCompoundRequest {
    @NotNull
    private AddBookEverywhereRequest addBookEverywhere;
    @NotNull
    private CreatePatronRequest patron;
    private Integer noOfDays;

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
