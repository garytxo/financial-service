package com.murray.financial.adapter.impl;

import com.murray.financial.adapter.AccountsAdapter;
import com.murray.financial.domain.entity.AccountTransfer;
import com.murray.financial.domain.entity.BankAccount;
import com.murray.financial.domain.enums.AccountCurrency;
import com.murray.financial.domain.repository.query.*;
import com.murray.financial.dtos.BankAccountDTO;
import com.murray.financial.dtos.BankAccountSearchCriteriaDTO;
import com.murray.financial.dtos.TransferDTO;
import com.murray.financial.dtos.TransferSearchCriteriaDTO;
import com.murray.financial.exceptions.TransferCreationException;
import com.murray.financial.querybuilder.*;
import com.murray.financial.service.AccountService;
import org.springframework.core.convert.ConversionService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * {@inheritDoc}
 */
@Transactional
public class AccountsAdapterImpl implements AccountsAdapter {

    private final ConversionService conversionService;

    private final AccountService accountService;

    public AccountsAdapterImpl(ConversionService conversionService, AccountService accountService) {
        this.conversionService = conversionService;
        this.accountService = accountService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BankAccountDTO create(final BankAccountDTO dto) {

        BankAccount bankAccount = accountService
                .saveBankAccount(dto.getIbanNumber(),
                        AccountCurrency.toCurrency(dto.getCurrency()),
                        dto.getBalance());

        return conversionService.convert(bankAccount, BankAccountDTO.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BankAccountDTO update(BankAccountDTO bankAccountDTO) {

        BankAccount updated = accountService.updateAccount(conversionService.convert(bankAccountDTO, BankAccount.class));

        return conversionService.convert(updated, BankAccountDTO.class);
    }

    @Override
    public void delete(String ibanNumber) {

        accountService.deleteAccount(ibanNumber);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BankAccountDTO> findAccountsBy(BankAccountSearchCriteriaDTO dto) {

        BankAccountSearch search = new BankAccountSearch();

        queryCondition(dto.getCurrency(), BankAccountQueryField.CURRENCY).ifPresent(
                search::addCondition
        );

        queryCondition(dto.getIbanNumber(), BankAccountQueryField.IBAN_NUMBER).ifPresent(
                search::addCondition
        );

        queryCondition(dto.getBalance(), BankAccountQueryField.BALANCE).ifPresent(
                search::addCondition
        );

        //order
        QueryField orderByFld = search.queryFields().stream()
                .filter(f -> f.getName().equalsIgnoreCase(dto.getOrderBy()))
                .findFirst().orElse(null);

        orderCondition(orderByFld, dto.getSortOrder()).ifPresent(
                search::setOrderCondition
        );


        return convertAccountsToDTO(accountService.findBankAccountsBy(search));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TransferDTO> findTransfersBy(TransferSearchCriteriaDTO dto) {

        TransferSearch search = new TransferSearch();

        queryCondition(dto.getSource(), TransferQueryField.SOURCE).ifPresent(
                search::addCondition
        );

        queryCondition(dto.getDestination(), TransferQueryField.DESTINATION).ifPresent(
                search::addCondition
        );

        //order
        QueryField orderByFld = search.queryFields().stream()
                .filter(f -> f.getName().equalsIgnoreCase(dto.getOrderBy()))
                .findFirst().orElse(null);

        orderCondition(orderByFld, dto.getSortOrder()).ifPresent(
                search::setOrderCondition
        );


        return convertTransfersToDTO(accountService.findTransfersBy(search));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TransferDTO createTransfer(TransferDTO transferDTO) {

        BankAccount sourceAccount = accountService.findAccountBy(transferDTO.getSource())
                .orElseThrow(() -> new TransferCreationException("Could not find source account for id:" + transferDTO.getSource()));

        BankAccount destinationAccount = accountService.findAccountBy(transferDTO.getDestination())
                .orElseThrow(() -> new TransferCreationException("Could not find destination account for id:" + transferDTO.getDestination()));

        if (sourceAccount.equals(destinationAccount)) {
            throw new TransferCreationException("Cannot transfer money to the same account");
        }

        AccountTransfer accountTransfer = accountService.createAccountTransfer(sourceAccount,
                destinationAccount, transferDTO.getAmount(), transferDTO.getDescription());

        return conversionService.convert(accountTransfer, TransferDTO.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TransferDTO executeTransfer(Long transferId) {


        AccountTransfer transfer =  accountService.executeTransfer(transferId);

        return conversionService.convert(transfer,TransferDTO.class);
    }

    /**
     * Convert the {@link BankAccountResult} into their corresponding {@link BankAccountDTO}
     *
     * @param bankAccountResults
     * @return list of  {@link BankAccountDTO}
     */
    private List<BankAccountDTO> convertAccountsToDTO(List<BankAccountResult> bankAccountResults) {

        List<BankAccountDTO> dtos = new ArrayList<>();
        bankAccountResults.forEach(
                result -> {
                    dtos.add(conversionService.convert(result, BankAccountDTO.class));
                }
        );

        return dtos;

    }


    /**
     * Convert the {@link TransferAccountResult} into their corresponding {@link TransferDTO}
     * @param results
     * @return list of  {@link TransferDTO}
     */
    private List<TransferDTO> convertTransfersToDTO(List<TransferAccountResult> results) {

        List<TransferDTO> dtos = new ArrayList<>();
        results.forEach(

                result -> {

                    dtos.add(conversionService.convert(result, TransferDTO.class));
                }
        );

        return dtos;
    }


    /**
     * Build the query ordering condition
     */
    private Optional<OrderCondition> orderCondition(QueryField orderByField,
                                                    String sortOrder) {

        if (Objects.nonNull(orderByField)) {

            OrderCondition orderCondition = new OrderCondition(orderByField);
            orderCondition.setOrderBy(toOrderBy(sortOrder));
            return Optional.ofNullable(orderCondition);
        }

        return Optional.empty();
    }

    private OrderBy toOrderBy(String sorderOrder) {

        return OrderBy.findBy(sorderOrder)
                .orElse(OrderBy.DESC);

    }


    /**
     * Converts the dto value into a {@link QueryCondition} that is appended
     * to the search query
     *
     * @param searchValue value
     * @param queryField  {@link QueryField}
     * @return {@link QueryCondition}
     */
    private Optional<QueryCondition> queryCondition(final String searchValue, final QueryField queryField) {

        if (!StringUtils.isEmpty(searchValue)) {
            QueryCondition condition = new QueryCondition(
                    queryField, Operator.EQUALS, searchValue);

            return Optional.ofNullable(condition);
        }

        return Optional.empty();

    }

    private Optional<QueryCondition> queryCondition(final BigDecimal searchValue, final QueryField queryField) {

        if (Optional.ofNullable(searchValue).isPresent()) {

            QueryCondition condition = new QueryCondition(
                    queryField, Operator.EQUALS, searchValue);

            return Optional.ofNullable(condition);
        }


        return Optional.empty();

    }


}
