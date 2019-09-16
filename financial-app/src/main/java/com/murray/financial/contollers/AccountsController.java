package com.murray.financial.contollers;

import com.murray.financial.adapter.AccountsAdapter;
import com.murray.financial.dtos.BankAccountDTO;
import com.murray.financial.dtos.BankAccountSearchCriteriaDTO;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Spring MVC REST Controller for the Accounts The only responsibility of the Controller is to define
 * the contract and add the Swagger documentation.
 * Apart from that, it should only call the Adapter method and return the resulting objects.
 *
 * @see <a href="http://localhost:8080/swagger-ui.html">Swagger UI</a>
 */
@Api(value = "Account operations",
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE
)
@RestController
@RequestMapping("/Account")
public class AccountsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountsController.class);

    private final AccountsAdapter accountsAdapter;

    public AccountsController(AccountsAdapter accountsAdapter) {
        this.accountsAdapter = accountsAdapter;
    }

    @GetMapping
    @ApiOperation(value = "search for account using defined search field and values")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "when successfully completed and return search results", response = BankAccountDTO.class),
            @ApiResponse(code = 404, message = "when no results match crieria"),
            @ApiResponse(code = 400, message = "Error occurred while searching  for accounts")
    })
    @ResponseStatus(code = HttpStatus.OK)
    public List<BankAccountDTO> readAccounts(
            @ApiParam(value = "Valid search criteria to filter for bank accounts")
                    BankAccountSearchCriteriaDTO searchCriteriaDTO
    ) {

        LOGGER.info("find with search accounts:{}", searchCriteriaDTO);
        return accountsAdapter.findAccountsBy(searchCriteriaDTO);
    }

    @PostMapping
    @ApiOperation(value = "create new bank account, minimum field required is currency")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "bank account successfully created", response = BankAccountDTO.class),
            @ApiResponse(code = 406, message = "Error occurred creating bank account")
    })
    @ResponseStatus(code = HttpStatus.CREATED)
    public BankAccountDTO createAccount(
            @ApiParam(value = "The new bank account details to create", example = "currency:EUR")
            @RequestBody BankAccountDTO bankAccountDTO) {

        LOGGER.info("Create account:{}", bankAccountDTO);


        return accountsAdapter.create(bankAccountDTO);
    }


    @PutMapping(value = "/{id}")
    @ApiOperation(value = "update a bank account status or currency")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "bank account successfully updated", response = BankAccountDTO.class),
            @ApiResponse(code = 406, message = "Error occurred updating bank account"),

    })
    @ResponseStatus(code = HttpStatus.OK)
    public BankAccountDTO updateAccount(
            @ApiParam(value = "The account's unique IBAN number to update")
            @PathVariable("id") String ibanNUmber,
            @ApiParam(value = "BankAccount details that should be updated ")
            @RequestBody BankAccountDTO bankAccountDTO) {

        LOGGER.info("update ibanNUmber:{} and  details:{}",ibanNUmber, bankAccountDTO);

        bankAccountDTO.setIbanNumber(ibanNUmber);
        return accountsAdapter.update(bankAccountDTO);
    }

    @DeleteMapping(path = {"/{id}"})
    @ApiOperation(value = "delete a bank account details")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "bank account successfully created"),
            @ApiResponse(code = 400, message = "Error occurred updating bank account"),

    })
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<?> deleteAccount(
            @ApiParam(value = "The account's unique IBAN number")
            @PathVariable("id") String ibanNUmber
    ) {

        LOGGER.info("delete  account:{}", ibanNUmber);


        accountsAdapter.delete(ibanNUmber);

        return ResponseEntity.ok().build();
    }


}


