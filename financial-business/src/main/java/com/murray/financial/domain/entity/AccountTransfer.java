package com.murray.financial.domain.entity;

import com.murray.financial.domain.repository.query.TransferAccountResult;
import com.murray.financial.exceptions.TransferCreationException;
import com.murray.financial.querybuilder.SearchableEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entity representation of a potential account transfer from a
 * source to a destination account for a specific getAmount.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "AccountTransfer")
@Table(name = "account_transfer")
@SqlResultSetMapping(name = TransferAccountResult.TRANSFER_ACCOUNT_RESULTS, classes = {
        @ConstructorResult(targetClass = TransferAccountResult.class,
                columns = {
                        @ColumnResult(name = "transferId", type = Long.class)
                        , @ColumnResult(name = "transferSent", type = LocalDateTime.class)
                        , @ColumnResult(name = "srcIbanNumber", type = String.class)
                        , @ColumnResult(name = "destIbanNumber", type = String.class)
                        , @ColumnResult(name = "amount", type = BigDecimal.class)
                })
})
public class AccountTransfer implements Serializable, SearchableEntity {

    /**
     * Internal unique id .
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * The source {@link BankAccount} where the getAmount should be debit from their
     * getBalance
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_account_id", nullable = false)
    private BankAccount source;

    /**
     * The source {@link BankAccount} where the getBalance is  credited  with the transfer getAmount.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_account_id", nullable = false)
    private BankAccount destination;

    /**
     * The transfer getAmount which will be move from the source to the destination
     */
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    /**
     * Time stamp when the actual transfer occurred
     */
    @Column(name = "timestamp", nullable = true)
    private LocalDateTime timeStamp;

    /**
     * The transfer description
     */
    @Column(name = "description", nullable = false)
    private String description;

    public AccountTransfer(BankAccount source, BankAccount destination, BigDecimal amount, String description) {

        validAccount(source, "Source account is not active");
        validAccount(destination, "Destination account is not active");

        this.source = source;
        this.destination = destination;
        this.amount = amount;
        this.description = description;
    }

    public AccountTransfer(BankAccount source, BankAccount destination, BigDecimal amount, String description,
                           LocalDateTime timeStamp) {

        this(source, destination, amount, description);
        this.timeStamp = timeStamp;
    }

    /**
     * Validates the transfer getAmount to ensure its a positive value and that the source
     * account has this getAmount to transfer.
     *
     * @param source {@link BankAccount}  to check getAmount against.
     * @param amount {@link BigDecimal}
     */
    private void validAmount(BankAccount source, BigDecimal amount) {

        if (Objects.isNull(amount) || amount.signum() <= 0) {
            throw new TransferCreationException("Transfer getAmount is a negative getAmount");
        }
        BigDecimal balance = source.getBalance();

        if (balance.compareTo(amount) < 0) {
            throw new TransferCreationException("Source account has not enough funds");

        }

    }


    /**
     * Vaidate a {@link BankAccount} is active and thrown {@link IllegalArgumentException} when
     * it's not active with a personalized error message.
     *
     * @param source       {@link BankAccount}  to validate.
     * @param errorMessage
     */
    private void validAccount(BankAccount source, String errorMessage) {

        if (!source.isActive()) {
            throw new TransferCreationException(errorMessage);
        }
    }


}
