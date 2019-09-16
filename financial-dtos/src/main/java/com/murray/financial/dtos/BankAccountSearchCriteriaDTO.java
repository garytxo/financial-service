package com.murray.financial.dtos;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Bank account search criteria
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "BankSearchCriteriaDTO",
        description = "defines optonal search criteria that can be use to" +
                "search for bank accounts."
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountSearchCriteriaDTO {


    @JsonProperty(value = "ibanNumber",defaultValue = "")
    @ApiModelProperty(value = "filter by  IBAN number",
            example = "ES23S020903200500041045040A111")
    private String ibanNumber;

    @JsonProperty(value = "currency")
    @ApiModelProperty(value = "filter by speicic currency",
            example = "EUR",
            allowableValues = "CHF, DKK, EUR, GBP, SEK, USD")
    private String currency;


    @JsonProperty(value = "balance")
    @ApiModelProperty(value = "filter by balance", example = "100")
    private BigDecimal balance;


    @JsonProperty(value = "orderBy")
    @ApiModelProperty(value = "order results by ibanNumber, currency, balance ",
            allowableValues = "ibanNumber, currency, balance",example = "balance",
            allowEmptyValue = true)
    private String orderBy;

    @JsonProperty(value = "sortOrder")
    @ApiModelProperty(value = "Order the orderBy field descending or ascending, default is ascending",
            allowableValues = "desc,asc",example = "desc",
            allowEmptyValue = true)
    private String sortOrder;

}
