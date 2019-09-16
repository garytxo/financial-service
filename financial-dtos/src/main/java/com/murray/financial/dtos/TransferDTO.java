package com.murray.financial.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Transfer transfer data object
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "TransferDTO",
        description = "A represents a monetary transfer between to active bank account"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferDTO {

    @JsonProperty(value = "id")
    @ApiModelProperty(value = "Transfer unique id ",
            required = true,
            example = "12121"
    )
    private Long id;

    @JsonProperty(value = "source")
    @ApiModelProperty(value = "Source bank account IBAN number ",
            required = true,
            example = "ES23S020903200500041045040A111"
    )
    private String source;

    @JsonProperty(value = "destination")
    @ApiModelProperty(value = "Destination bank account IBAN number", required = true,
            example = "ES23S020903200500041045040A111")
    private String destination;

    @JsonProperty(value = "amount")
    @ApiModelProperty(value = "Amount of money to be transfer fron the source account to the destination ",
            example = "100",
            required = true)
    private BigDecimal amount;

    @JsonProperty(value = "description")
    @ApiModelProperty(value = "Transfer description", required = true,
            example = "Salary November 2018")
    private String description;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @JsonProperty(value = "executionTime")
    @ApiModelProperty(value = "Date and time transfer took place")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime executionTime;

    @JsonProperty(value = "status")
    @ApiModelProperty(value = "Defines the status after the transfer was executed")
    private String status;

}
