package com.gakshintala.bookmybook.usecases.patron;

import com.gakshintala.bookmybook.domain.library.AvailableBook;
import com.gakshintala.bookmybook.domain.patron.Patron;
import com.gakshintala.bookmybook.ports.persistence.library.FindAvailableBook;
import com.gakshintala.bookmybook.ports.persistence.library.HandlePatronEventInLibrary;
import com.gakshintala.bookmybook.ports.persistence.patron.FindPatron;
import com.gakshintala.bookmybook.ports.persistence.patron.HandlePatronEvent;
import io.vavr.control.Try;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.gakshintala.bookmybook.fixtures.BookFixture.circulatingAvailableBook;
import static com.gakshintala.bookmybook.fixtures.PatronFixture.regularPatron;

class PatronPlaceBookOnHoldTest {
    AvailableBook circulatingAvailableBook = circulatingAvailableBook();
    Patron regularPatron = regularPatron();
    
    // Stubs
    private final FindAvailableBook findAvailableBook = id -> Try.of(() -> circulatingAvailableBook);
    private final FindPatron findPatron = id -> Try.of(() -> regularPatron);
    private final HandlePatronEvent handlePatronEvent = patronEvent -> Try.of(() -> regularPatron);
    private final HandlePatronEventInLibrary handlePatronEventInLibrary = patronEvent -> Try.of(() -> circulatingAvailableBook.getBookInstanceId());
    
    @Test
    @DisplayName("Regular Patron Successfully places circulating book on hold")
    void regularPatronPlaceBookOnHold() {
        
    }
}