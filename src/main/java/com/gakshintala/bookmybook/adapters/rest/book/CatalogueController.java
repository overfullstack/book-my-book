package com.gakshintala.bookmybook.adapters.rest.book;

import com.gakshintala.bookmybook.core.usecases.AddBookInstanceToCatalogueUseCase;
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
    private UseCaseExecutor useCaseExecutor;
    private AddBookToCatalogueUseCase addBookToCatalogueUseCase;
    private AddBookInstanceToCatalogueUseCase addBookInstanceToCatalogueUseCase;

    @Autowired
    public CatalogueController(UseCaseExecutor useCaseExecutor, AddBookToCatalogueUseCase addBookToCatalogueUseCase, 
                               AddBookInstanceToCatalogueUseCase addBookInstanceToCatalogueUseCase) {
        this.useCaseExecutor = useCaseExecutor;
        this.addBookToCatalogueUseCase = addBookToCatalogueUseCase;
        this.addBookInstanceToCatalogueUseCase = addBookInstanceToCatalogueUseCase;
    }

    @Override
    public CompletableFuture<ResponseEntity<ApiResponse>> addBookToCatalogue(@Valid AddBookRequest addBookRequest,
                                                                             HttpServletRequest httpServletRequest) {
        return useCaseExecutor.execute(
                addBookToCatalogueUseCase,
                addBookRequest.toInput(),
                outputValues -> ApiResponse.fromResult(outputValues.getResult())
        );
    }

    @Override
    public CompletableFuture<CatalogueBookResponse> addBookInstanceToCatalogue(@Valid AddBookInstanceRequest addBookInstanceRequest, 
                                                                                     HttpServletRequest httpServletRequest) {
        return useCaseExecutor.execute(
                addBookInstanceToCatalogueUseCase,
                addBookInstanceRequest.toInput(),
                outputValues -> CatalogueBookResponse.fromResult(outputValues.getCatalogueBookInstance())
        );
    }
}
