package com.murray.financial.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Transfer search criteria
 */
@ApiModel(value = "TransferSearchCriteriaDTO",
        description = "defines optonal search criteria that can be use to" +
                "search for account transfers."
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferSearchCriteriaDTO {

    @JsonProperty(value = "source")
    @ApiModelProperty(value = "filter on source bank account IBAN number ", required = true)
    private String source;

    @JsonProperty(value = "destination")
    @ApiModelProperty(value = "filter on destination bank account IBAN number ", required = true)
    private String destination;


    @JsonProperty(value = "orderBy")
    @ApiModelProperty(value = "order results by  source, destination ",
            allowableValues = "source, destination",
            allowEmptyValue = true)
    private String orderBy;

    @JsonProperty(value = "sortOrder")
    @ApiModelProperty(value = "Order the orderBy field descending or ascending, default is ascending",
            allowableValues = "desc,asc",
            allowEmptyValue = true)
    private String sortOrder;

}
