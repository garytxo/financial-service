package com.murray.financial.service;

import com.murray.financial.domain.entity.AccountTransfer;
import com.murray.financial.domain.entity.BankAccount;
import com.murray.financial.domain.enums.AccountCurrency;
import com.murray.financial.domain.repository.query.BankAccountResult;
import com.murray.financial.domain.repository.query.BankAccountSearch;
import com.murray.financial.domain.repository.query.TransferAccountResult;
import com.murray.financial.domain.repository.query.TransferSearch;
import com.murray.financial.exceptions.AccountCreationException;
import com.murray.financial.querybuilder.QueryCondition;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Account service layer handles the creation of accounts and it's different
 * types of getTransactions.
 */
public interface AccountService {


    /**
     * Creates a new active {@link BankAccount} using the account unqiue IBAN
     * number.
     *
     * @param ibanNumber     account unique identifier number
     * @param currency       account getCurrency
     * @param openingDeposit initial getAmount of the first transaction made on opening the account.
     * @return {@link BankAccount}
     * @throws AccountCreationException
     */
    BankAccount saveBankAccount(final String ibanNumber, final AccountCurrency currency,
                                final BigDecimal openingDeposit) throws AccountCreationException;


    /**
     * Find {@link BankAccount} by internal id number.
     *
     * @param id Long
     * @return {@link BankAccount}
     */
    Optional<BankAccount> findAccountById(final Long id);


    /**
     * Find {@link BankAccount} by unique iban number
     *
     * @param ibanNumber string iban number
     * @return {@link BankAccount}
     */
    Optional<BankAccount> findAccountBy(final String ibanNumber);


    /**
     * Updates the {@link BankAccount} status and currency
     *
     * @param bankAccount {@link BankAccount}
     */
    BankAccount updateAccount(final BankAccount bankAccount);


    /**
     * Save bank account entity
     *
     * @param bankAccount {@link BankAccount}
     * @return {@link BankAccount}
     */
    BankAccount saveAccount(final BankAccount bankAccount);


    /**
     * Perform a soft which sets the back account getStatus to DELETE:
     *
     * @param ibanNumber
     */
    void deleteAccount(final String ibanNumber);

    /**
     * Account​ balances are updated due to an Operational Banking Tax
     *
     * @param taxRate
     */
    void updateAccountBalancesWith(final BigDecimal taxRate);


    /**
     * Find {@link BankAccountResult}s using the specific {@link QueryCondition}s ,
     * and OrderBy condition
     *
     * @param searchQuery {@link BankAccountSearch} which is used to generate the native query
     * @return list of {@link BankAccountResult}
     */
    List<BankAccountResult> findBankAccountsBy(final BankAccountSearch searchQuery);


    /**
     * Save or updates a {@link AccountTransfer}
     *
     * @param accountTransfer {@link AccountTransfer}
     * @return {@link AccountTransfer}
     */
    AccountTransfer persistTransfer(final AccountTransfer accountTransfer);

    /**
     * Create a new transfer that will occur between to valid back account in which the source
     * would be debited and the destination credited
     *
     * @param source      {@link BankAccount}
     * @param destination {@link BankAccount}
     * @param amount      {@link BigDecimal} represents the amout to be debited
     * @param description description of the transfer
     * @return com.murray.financial.domain.entity.AccountTransfer
     */
    AccountTransfer createAccountTransfer(BankAccount source, BankAccount destination,
                                          BigDecimal amount, String description);

    /**
     * Find {@link TransferAccountResult} based on the {@link TransferSearch} query fields
     * and conditions
     *
     * @param transferSearch {@link TransferSearch} which is used to generate the native query
     * @return list of {@link TransferAccountResult}
     */
    List<TransferAccountResult> findTransfersBy(final TransferSearch transferSearch);

    /**
     * finds the {@link AccountTransfer} corresponding to the internal id , validates
     * to ensure that the accounts are still active then debits the source account and
     * credits the destination account.  <br />
     * If a ​Transfer​ is done between two different currencies, the specific currency is defined
     * by the destination ​Account​ and the exchange rate is got from:
     * o https://api.exchangeratesapi.io/latest?base=<baseCode>&symbols=<currencyCode> (p.e. https://api.exchangeratesapi.io/latest?base=GBP&symbols=EUR​)
     *
     * @param id {@link AccountTransfer} unique id
     * @return {@link AccountTransfer}
     */
    AccountTransfer executeTransfer(final Long id);
}
