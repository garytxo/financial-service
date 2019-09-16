package com.murray.financial.adapter.converter;

import com.murray.financial.domain.repository.query.TransferAccountResult;
import com.murray.financial.dtos.TransferDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts the TransferAccountResult entity model to it's TransferDTO
 */
@Component
public class TransferAccountResultToDto implements Converter<TransferAccountResult, TransferDTO> {


    @Override
    public TransferDTO convert(TransferAccountResult domain) {

        TransferDTO dto = new TransferDTO();
        dto.setId(domain.getTransferId());
        dto.setAmount(domain.getAmount());
        dto.setSource(domain.getSourceAccountIbanNumber());
        dto.setDestination(domain.getDestIbanAccountIbanNumber());
        dto.setExecutionTime(domain.getTransferSent());


        return dto;
    }
}
