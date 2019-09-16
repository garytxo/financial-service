package com.murray.financial.service.impl;

import com.murray.financial.domain.entity.AccountTransfer;
import com.murray.financial.domain.entity.BankAccount;
import com.murray.financial.domain.entity.Transaction;
import com.murray.financial.domain.enums.AccountCurrency;
import com.murray.financial.domain.enums.AccountStatus;
import com.murray.financial.domain.repository.AccountTransferJPARespository;
import com.murray.financial.domain.repository.BackAccountJPARepository;
import com.murray.financial.domain.repository.query.BankAccountResult;
import com.murray.financial.domain.repository.query.BankAccountSearch;
import com.murray.financial.domain.repository.query.TransferAccountResult;
import com.murray.financial.domain.repository.query.TransferSearch;
import com.murray.financial.exceptions.AccountCreationException;
import com.murray.financial.exceptions.NotFoundException;
import com.murray.financial.exceptions.TransferCreationException;
import com.murray.financial.service.AccountService;
import com.murray.financial.service.CurrencyConverter;
import com.murray.financial.service.untils.IBANNumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * {@inheritDoc}
 */
public class BankAccountServiceImpl implements AccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankAccountServiceImpl.class);
    private static final String OPENING_DEPOSIT = "Opening deposit";

    private final BackAccountJPARepository backAccountRepository;
    private final AccountTransferJPARespository accountTransferJPARespository;
    private final CurrencyConverter currencyConverter;

    public BankAccountServiceImpl(BackAccountJPARepository backAccountRepository, AccountTransferJPARespository accountTransferJPARespository, CurrencyConverter currencyConverter) {
        this.backAccountRepository = backAccountRepository;
        this.accountTransferJPARespository = accountTransferJPARespository;
        this.currencyConverter = currencyConverter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AccountTransfer createAccountTransfer(BankAccount source, BankAccount destination, BigDecimal amount, String description) throws TransferCreationException {

        try {

            return accountTransferJPARespository.save(new AccountTransfer(source, destination, amount, description));

        } catch (IllegalArgumentException e) {
            throw new TransferCreationException(e);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AccountTransfer executeTransfer(final Long id) {

        AccountTransfer transfer = accountTransferJPARespository.findById(id)
                .orElseThrow(() -> new TransferCreationException("No transfer found for id:" + id));

        if (!transfer.getSource().isActive() || !transfer.getDestination().isActive()) {
            throw new TransferCreationException("One or both transfer accounts are not active");
        }


        BigDecimal debitAmount = transfer.getAmount();

        LOGGER.info("Debit source account {} of {} {}", transfer.getSource().getId(), debitAmount, transfer.getSource().getCurrency());
        Transaction debit = createTransaction(debitAmount.negate(), transfer.getDescription());
        transfer.getSource().add(debit);

        BigDecimal creditAmount = currencyConverter.convertAmountTo(debitAmount, transfer.getDestination().getCurrency(), transfer.getSource().getCurrency());

        LOGGER.info("Credit destination account {} of {} {}", transfer.getDestination().getId(), creditAmount, transfer.getDestination().getCurrency());
        Transaction credit = createTransaction(creditAmount, transfer.getDescription());
        transfer.getDestination().add(credit);

        if (Objects.isNull(transfer.getTimeStamp())) {
            transfer.setTimeStamp(LocalDateTime.now());
        }

        return accountTransferJPARespository.save(transfer);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public AccountTransfer persistTransfer(AccountTransfer accountTransfer) {

        return accountTransferJPARespository.save(accountTransfer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BankAccount saveBankAccount(final String ibanNumber, final AccountCurrency currency,
                                       final BigDecimal openingDeposit) throws AccountCreationException {


        try {
            LOGGER.debug("Creating account with {} and {}", ibanNumber, currency);
            String cleanIbanNumber = validateIbanNumber(ibanNumber, currency);

            BankAccount bankAccount =
                    backAccountRepository.save(new BankAccount(cleanIbanNumber,
                            currency, createTransaction(openingDeposit, OPENING_DEPOSIT)));

            return bankAccount;

        } catch (DataIntegrityViolationException dataEx) {
            throw new AccountCreationException("Data Integrity exception", dataEx);
        }
    }

    /**
     * Create an account  {@link Transaction} corresponding to the getAmount
     *
     * @param amount
     */
    private Transaction createTransaction(final BigDecimal amount, final String description) {

        if (Objects.isNull(amount))
            return new Transaction(BigDecimal.ZERO, description);

        return new Transaction(amount, description);
    }


    /**
     * Check the string format iban number using  apache commons
     *
     * @param accountNumber
     * @throws AccountCreationException
     */
    private String validateIbanNumber(String accountNumber, final AccountCurrency currency) throws AccountCreationException {

        if (StringUtils.isEmpty(accountNumber)) {
            accountNumber = IBANNumberUtils.createIBANNumber(currency.randomCountryCode());
        }

        final String cleanedIbanNumber = clean(accountNumber);

        if (!IBANNumberUtils.isValidIBANumber(cleanedIbanNumber)) {
            throw new AccountCreationException("Invalid iban number");
        }

        return cleanedIbanNumber;
    }

    /**
     * cleans the Iban Number whist spaces found in the account number.
     *
     * @param ibanNumber
     */
    private String clean(final String ibanNumber) {

        return ibanNumber.replaceAll("\\s+", "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<BankAccount> findAccountById(Long id) {
        return backAccountRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BankAccount saveAccount(BankAccount bankAccount) {
        return backAccountRepository.save(bankAccount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<BankAccount> findAccountBy(String ibanNumber) {

        if (StringUtils.isEmpty(ibanNumber)) {
            return Optional.empty();
        }

        BankAccount account = backAccountRepository.findFirstByIbanNumberEquals(ibanNumber);

        return Optional.ofNullable(account);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BankAccount updateAccount(BankAccount bankAccount) {

        BankAccount original = getOriginalEntity(bankAccount).orElseThrow(
                () -> new IllegalArgumentException("Could not find original bank account for updating")
        );

        original.setCurrency(bankAccount.getCurrency());
        original.setStatus(bankAccount.getStatus());

        return backAccountRepository.save(original);
    }

    /**
     * Find there already exist a {@link BankAccount} and return its for editing
     */
    private Optional<BankAccount> getOriginalEntity(BankAccount bankAccount) {

        if (Objects.nonNull(bankAccount.getId())) {
            return backAccountRepository.findById(bankAccount.getId());
        }

        if (!StringUtils.isEmpty(bankAccount.getIbanNumber())) {

            return Optional.ofNullable(
                    backAccountRepository.findFirstByIbanNumberEquals(bankAccount.getIbanNumber()));
        }

        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BankAccountResult> findBankAccountsBy(final BankAccountSearch searchQuery) {

        return backAccountRepository.searchBy(searchQuery);

    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void updateAccountBalancesWith(BigDecimal taxRate) {

        LOGGER.info("updating account balances with tax rate:{}", taxRate);

        backAccountRepository.findAll()
                .forEach(ac -> addTaxRateTransactionTo(ac, taxRate));


    }

    /**
     * Calculates the operation tax rate from the account balance and then
     * adds a new transaction to the account
     *
     * @param account {@link BankAccount}
     * @param taxRate rate which the calculation is performed
     */
    private void addTaxRateTransactionTo(final BankAccount account, final BigDecimal taxRate) {

        BigDecimal balance = account.getBalance();
        BigDecimal newTransaction = account.getBalance().multiply(taxRate);

        LOGGER.info("account :{} balance:{} newTransaction:{}  ",
                account.getIbanNumber(),
                balance, newTransaction);
        Transaction rateTransaction =
                new Transaction(newTransaction, "Operational Banking Tax");

        account.add(rateTransaction);
        saveAccount(account);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAccount(String ibanNumber) {

        BankAccount account = backAccountRepository.findFirstByIbanNumberEquals(ibanNumber);
        if (Objects.isNull(account)) {

            throw new NotFoundException("Cound not find account", ibanNumber);
        }

        account.setStatus(AccountStatus.DELETED);

        backAccountRepository.save(account);
    }


    @Override
    public List<TransferAccountResult> findTransfersBy(TransferSearch transferSearch) {

        return accountTransferJPARespository.searchBy(transferSearch);
    }
}
