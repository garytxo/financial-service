package com.murray.financial.domain.entity;


import com.murray.financial.domain.enums.AccountCurrency;
import com.murray.financial.domain.enums.AccountStatus;
import com.murray.financial.domain.repository.query.BankAccountResult;
import com.murray.financial.querybuilder.SearchableEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represent a entity's bank account in the financial system.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "transactions")
@ToString(exclude = "transactions")
@Entity(name = "BankAccount")
@Table(name = "bank_account")
@SqlResultSetMapping(name = BankAccountResult.BANK_ACCOUNT_RESULTS, classes = {
        @ConstructorResult(targetClass = BankAccountResult.class,
                columns = {
                        @ColumnResult(name = "openedOn", type = LocalDate.class)
                        , @ColumnResult(name = "ibanNumber", type = String.class)
                        , @ColumnResult(name = "balance", type = BigDecimal.class)
                        , @ColumnResult(name = "currency", type = String.class)
                        , @ColumnResult(name = "status", type = String.class)
                })
})
public class BankAccount implements Serializable, SearchableEntity {

    /**
     * Internal unique id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * The account unique iban number
     */
    @Column(name = "iban_number", nullable = false, unique = true)
    private String ibanNumber;

    /**
     * Defines the account's actual getCurrency in which are transaction are performed
     */
    @Column(name = "currency", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountCurrency currency;

    /**
     * Returns the actual date when the account was opened.
     */
    @Column(name = "open_on")
    private LocalDate openedOn;

    /**
     * Defines  the account's actual getStatus
     */
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    /**
     * List of monetary getTransactions that make the account getBalance
     */
    @OneToMany(mappedBy = "bankAccount", cascade = CascadeType.ALL)
    private Set<Transaction> transactions = new LinkedHashSet<>();

    public BankAccount(final String ibanNumber, final AccountCurrency currency) {
        this.ibanNumber = ibanNumber;
        this.currency = currency;
        this.openedOn = LocalDate.now();
        this.status = AccountStatus.ACTIVE;
    }

    public BankAccount(final Long id, final String iban, final AccountCurrency currency, final Transaction openingDeposit) {
        this.id = id;
        this.ibanNumber = iban;
        this.currency = currency;
        this.openedOn = LocalDate.now();
        this.status = AccountStatus.ACTIVE;
        this.add(openingDeposit);
    }

    public BankAccount(final String iban, final AccountCurrency currency, final Transaction openingDeposit) {
        this(iban, currency);
        this.add(openingDeposit);

    }


    /**
     * Returns the current account getBalance by summing it's transaction amounts
     */
    @Transient
    public BigDecimal getBalance() {

        return transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Adds a new account transaction
     *
     * @param transaction
     */
    public void add(final Transaction transaction) {

        if (Objects.nonNull(transaction) && transaction.getAmount().signum() != 0) {
            transaction.setBankAccount(this);
            this.transactions.add(transaction);
        }

    }

    /**
     * Boolean that indicates if the account is active or not.
     */
    public boolean isActive() {
        return Objects.nonNull(status) && status.equals(AccountStatus.ACTIVE);
    }


}
