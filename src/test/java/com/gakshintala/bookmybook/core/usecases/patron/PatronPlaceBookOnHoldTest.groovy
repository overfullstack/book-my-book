package com.gakshintala.bookmybook.core.usecases.patron

import com.gakshintala.bookmybook.core.domain.catalogue.CatalogueBookInstanceId
import com.gakshintala.bookmybook.core.domain.library.AvailableBook
import com.gakshintala.bookmybook.core.domain.patron.Patron
import com.gakshintala.bookmybook.core.domain.patron.PatronEvent
import com.gakshintala.bookmybook.core.domain.patron.PatronId
import com.gakshintala.bookmybook.core.ports.repositories.library.FindAvailableBook
import com.gakshintala.bookmybook.core.ports.repositories.library.HandlePatronEventInLibrary
import com.gakshintala.bookmybook.core.ports.repositories.patron.FindPatron
import com.gakshintala.bookmybook.core.ports.repositories.patron.HandlePatronEvent
import io.vavr.Tuple3
import io.vavr.control.Try
import spock.lang.Specification

import static com.gakshintala.bookmybook.core.domain.patron.PatronEvent.BookPlacedOnHold
import static com.gakshintala.bookmybook.fixtures.BookFixture.*
import static com.gakshintala.bookmybook.fixtures.PatronFixture.*

class PatronPlaceBookOnHoldTest extends Specification {
    Patron regularPatron = regularPatron()
    Patron researcherPatron = researcherPatron(anyPatronId())
    AvailableBook circulatingAvailableBook = circulatingAvailableBook()
    AvailableBook restrictedAvailableBook = restrictedBook()
    
    FindAvailableBook willFindCirculatingBook = { id -> Try.of({ -> circulatingAvailableBook }) }
    FindAvailableBook willFindRestrictedBook = { id -> Try.of({ -> restrictedAvailableBook }) }
    
    FindPatron willFindRegularPatron = { id -> Try.of({ -> regularPatron }) }
    FindPatron willFindResearcherPatron = { id -> Try.of({ -> researcherPatron }) }
    
    HandlePatronEvent handlePatronEvent = { patronEvent -> Try.of({ -> regularPatron }) }
    HandlePatronEventInLibrary handlePatronEventInLibrary = { patronEvent ->
        Try.of({ -> circulatingAvailableBook.getBookInformation().getCatalogueBookInstanceId() })
    }

    def 'should successfully place on hold book if patron and book exist'() {
        given:
        PatronPlaceBookOnHold patronPlaceBookOnHold = new PatronPlaceBookOnHold(willFindCirculatingBook, willFindRegularPatron, 
                handlePatronEvent, handlePatronEventInLibrary)
        
        when:
        Try<Tuple3<PatronEvent, Patron, CatalogueBookInstanceId>> result = patronPlaceBookOnHold.execute(for3days(anyPatron()))
        
        then:
        result.isSuccess()
        result.get()._1() in BookPlacedOnHold
    }

    PatronPlaceBookOnHold.PlaceOnHoldCommand for3days(PatronId patronId) {
        return PatronPlaceBookOnHold.PlaceOnHoldCommand.closeEnded(patronId, anyBookId(), 3)
    }
    
    def 'regular patron should not be able to hold restricted book'() {
        given:
        PatronPlaceBookOnHold patronPlaceBookOnHold = new PatronPlaceBookOnHold(willFindRestrictedBook, willFindRegularPatron, 
                handlePatronEvent, handlePatronEventInLibrary)
        
        when:
        Try<Tuple3<PatronEvent, Patron, CatalogueBookInstanceId>> result = patronPlaceBookOnHold.execute(for3days(anyPatron()))
        
        then:
        result.isSuccess()
        result.get()._1() in PatronEvent.BookHoldFailed
    }

    def 'research patron should be able to hold restricted book'() {
        given:
        PatronPlaceBookOnHold patronPlaceBookOnHold = new PatronPlaceBookOnHold(willFindRestrictedBook, willFindResearcherPatron,
                handlePatronEvent, handlePatronEventInLibrary)
        
        when:
        Try<Tuple3<PatronEvent, Patron, CatalogueBookInstanceId>> result = patronPlaceBookOnHold.execute(for3days(anyPatron()))
        
        then:
        result.isSuccess()
        result.get()._1() in BookPlacedOnHold
    }

}