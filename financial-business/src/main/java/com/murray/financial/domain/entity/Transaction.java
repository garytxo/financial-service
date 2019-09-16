package com.murray.financial.domain.entity;

import com.murray.financial.domain.enums.TransactionType;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * Financial representation of a bank account transaction which
 * credits or debits the current getBalance.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude="bankAccount")
@ToString(exclude ="bankAccount" )
@Entity(name = "Transaction")
@Table(name = "bank_account_transaction")
public class Transaction implements Serializable {

    /**
     * internal unique id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * time stamp when the transaction created
     */
    @Column(name = "created_on")
    private ZonedDateTime createdOn;

    /**
     * Type of transaction that occurred
     */
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    /**
     * Transaction getAmount
     */
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    /**
     * Description that explains the transaction
     */
    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_account_id")
    private BankAccount bankAccount;

    public Transaction(final BigDecimal amount, final String description) {
        this.amount = amount;
        this.description = description;
        this.createdOn = ZonedDateTime.now();
        this.defineTypeFor(amount);

    }

    /**
     * Defines the transaction getType based on the getAmount
     *
     * @param amount
     */
    private void defineTypeFor(final BigDecimal amount) {

        this.type = amount.signum() == 1 ? TransactionType.CREDIT : TransactionType.DEBIT;

    }


}
