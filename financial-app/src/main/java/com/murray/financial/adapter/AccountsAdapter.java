package com.murray.financial.adapter;

import com.murray.financial.dtos.BankAccountDTO;
import com.murray.financial.dtos.BankAccountSearchCriteriaDTO;
import com.murray.financial.dtos.TransferDTO;
import com.murray.financial.dtos.TransferSearchCriteriaDTO;

import java.util.List;

/**
 * Adapter for the {@link com.murray.financial.service.AccountService}. The only responsibility of the Adapter is to call the Service
 * and convert the resulting objects into DTOs. All the business logic is implemented in the Service.
 */
public interface AccountsAdapter {


    /**
     * Create a new Transfer that will transfer money from source account to
     * a destination account.
     *
     * @param transferDTO contains the transfer details
     * @return TransferDTO
     */
    TransferDTO createTransfer(TransferDTO transferDTO);


    /**
     * Executes a stored transfer which will move money from one account to another
     *
     * @param transferId transfer unique id
     * @return TransferDTO
     */
    TransferDTO executeTransfer(Long transferId);

    /**
     * Create a new active bank account using the {@link BankAccountDTO} details
     *
     * @param bankAccountDTO {@link BankAccountDTO}
     * @return {@link BankAccountDTO}
     */
    BankAccountDTO create(final BankAccountDTO bankAccountDTO);


    /**
     * Update a bank account using the {@link BankAccountDTO} details
     *
     * @param bankAccountDTO {@link BankAccountDTO}
     * @return {@link BankAccountDTO}
     */
    BankAccountDTO update(final BankAccountDTO bankAccountDTO);


    /**
     * Soft delete a bank account
     *
     * @param ibanNUmber
     */
    void delete(final String ibanNUmber);


    /**
     * find the bank account that match the search criteria defined in   {@link BankAccountSearchCriteriaDTO}.
     *
     * @param searchCriteriaDTO {@link BankAccountSearchCriteriaDTO}
     * @return list of {@link BankAccountDTO}
     */
    List<BankAccountDTO> findAccountsBy(final BankAccountSearchCriteriaDTO searchCriteriaDTO);


    /**
     * find the bank account  transfers that match the search criteria defined in
     * {@link TransferSearchCriteriaDTO}.
     *
     * @param searchCriteriaDTO {@link TransferSearchCriteriaDTO}
     * @return list of {@link TransferDTO}
     */
    List<TransferDTO> findTransfersBy(final TransferSearchCriteriaDTO searchCriteriaDTO);
}
