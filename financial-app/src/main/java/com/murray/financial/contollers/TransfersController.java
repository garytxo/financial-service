package com.murray.financial.contollers;

import com.murray.financial.adapter.AccountsAdapter;
import com.murray.financial.dtos.BankAccountDTO;
import com.murray.financial.dtos.TransferDTO;
import com.murray.financial.dtos.TransferSearchCriteriaDTO;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Spring MVC REST Controller for the Accounts Transfers that occur among active acccount.
 *
 * @see <a href="http://localhost:8080/swagger-ui.html">Swagger UI</a>
 */
@Api(value = "Account transfer operations",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE
)
@RestController
@RequestMapping("/Transfer")
public class TransfersController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransfersController.class);
    private final AccountsAdapter accountsAdapter;

    public TransfersController(AccountsAdapter accountsAdapter) {
        this.accountsAdapter = accountsAdapter;
    }

    @GetMapping
    @ApiOperation(value = "search for transfer using defined search field and values")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "when successfully completed and return search results", response = BankAccountDTO.class),
            @ApiResponse(code = 404, message = "when no results match crieria"),
            @ApiResponse(code = 400, message = "Error occurred whilst searching for transfers")
    })
    @ResponseStatus(code = HttpStatus.OK)
    public List<TransferDTO> readTransfer(
            @ApiParam(value = "The search criteria to filter for bank accounts")
                    TransferSearchCriteriaDTO searchCriteriaDTO
    ) {

        LOGGER.info("find transfers:{}", searchCriteriaDTO);
        return accountsAdapter.findTransfersBy(searchCriteriaDTO);
    }

    @PostMapping
    @ApiOperation(value = "create new transfer that can occur among to valid accounts")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "transfer was successfully created", response = BankAccountDTO.class),
            @ApiResponse(code = 406, message = "Error occurred creating transfer")
    })
    @ResponseStatus(code = HttpStatus.CREATED)
    public TransferDTO createTransfer(
            @ApiParam(value = "The transfer details to save")
            @RequestBody TransferDTO transferDTO) {


        LOGGER.info("Create transfer:{}", transferDTO);

        return accountsAdapter.createTransfer(transferDTO);

    }

}
