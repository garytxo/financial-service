package com.murray.financial.contollers;


import com.murray.financial.adapter.AccountsAdapter;
import com.murray.financial.dtos.TransferDTO;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Spring MVC REST Controller for the Accounts Transfers executions.
 *
 * @see <a href="http://localhost:8080/swagger-ui.html">Swagger UI</a>
 */
@Api(value = "Execute Account transfer operation",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE
)
@RestController
@RequestMapping("/Execute-Transfer")
public class ExecuteTransferController {


    private static final Logger LOGGER = LoggerFactory.getLogger(ExecuteTransferController.class);
    private final AccountsAdapter accountsAdapter;

    public ExecuteTransferController(AccountsAdapter accountsAdapter) {
        this.accountsAdapter = accountsAdapter;
    }

    @PutMapping(value = "/{transferId}")
    @ApiOperation(value = "execute a transfer monving money from a source account to destination account")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Result of the each transfer executed and getStatus", response = TransferDTO.class),
            @ApiResponse(code = 406, message = "Error occurred executing transfer")
    })
    @ResponseStatus(code = HttpStatus.OK)
    public TransferDTO executeTransfers(
            @ApiParam(value = "tranfer id  that should be executed")
            @PathVariable("transferId") Long transferId
    ) {

        LOGGER.info("execute transferId:{}", transferId);
        return accountsAdapter.executeTransfer(transferId);
    }
}
