package com.gakshintala.bookmybook.adapters.rest.book;

import com.gakshintala.bookmybook.core.usecases.AddBookToCatalogueUseCase;
import com.gakshintala.bookmybook.core.usecases.UseCaseExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.concurrent.CompletableFuture;

@RestController
public class CatalogueController implements CatalogueResource {
    private AddBookToCatalogueUseCase addBookToCatalogueUseCase;
    private UseCaseExecutor useCaseExecutor;

    @Autowired
    public CatalogueController(UseCaseExecutor useCaseExecutor, AddBookToCatalogueUseCase addBookToCatalogueUseCase) {
        this.useCaseExecutor = useCaseExecutor;
        this.addBookToCatalogueUseCase = addBookToCatalogueUseCase;
    }

    @Override
    public CompletableFuture<ResponseEntity<ApiResponse>> addBookToCatalogue(@Valid AddBookRequest request,
                                                                             HttpServletRequest httpServletRequest) {
        return useCaseExecutor.execute(
                addBookToCatalogueUseCase,
                request.toInput(),
                outputValues -> ApiResponse.fromResult(outputValues.getResult())
        );
    }
}
