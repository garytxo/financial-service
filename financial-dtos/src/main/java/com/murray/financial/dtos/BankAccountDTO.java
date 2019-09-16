package com.murray.financial.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Bank account data transfer data object
 */
@ApiModel(value = "BankAccountDTO",
        description = "A bank account is a financial account maintained by a bank for a customer"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountDTO {

    @JsonProperty(value = "ibanNumber")
    @ApiModelProperty(value = "Account's unique IBAN number ", example = "ES23S020903200500041045040A111")
    private String ibanNumber;

    @JsonProperty(value = "currency")
    @ApiModelProperty(value = " Account curreny which all monetary transactions are based ",
            required = true,
            allowableValues = "CHF, DKK, EUR, GBP, SEK, USD",
            example = "EUR",
            allowEmptyValue = true
    )
    private String currency;

    @JsonProperty(value = "openedOn")
    @ApiModelProperty(value = "Date when the account was opened ")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    // Formats output date when this DTO is passed through JSON
    @JsonFormat(pattern = "yyyy/MM/dd")
    // Allows dd/MM/yyyy date to be passed into GET request in JSON
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate openedOn;

    @JsonProperty(value = "status")
    @ApiModelProperty(value = "type of applied payment ",
            allowableValues = "ACTIVE, DISABLED, DELETED",
            example = "ACTIVE",
            allowEmptyValue = true)
    private String status;

    @JsonProperty(value = "balance")
    @ApiModelProperty(value = "Actual account balance",
            example = "100",allowEmptyValue = true)
    private BigDecimal balance;
}
