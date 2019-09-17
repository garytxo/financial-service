package com.murray.financial.adapter.converter;

import com.murray.financial.domain.entity.AccountTransfer;
import com.murray.financial.dtos.TransferDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Converts the {@link AccountTransfer} to a {@link TransferDTO}
 */
@Component
public class TransferDomainToDTO implements Converter<AccountTransfer, TransferDTO> {

    @Override
    public TransferDTO convert(AccountTransfer accountTransfer) {

        TransferDTO dto = new TransferDTO();
        dto.setId(accountTransfer.getId());

        dto.setAmount(accountTransfer.getAmount());
        dto.setDescription(accountTransfer.getDescription());

        if (Objects.nonNull(accountTransfer.getSource())) {
            dto.setSource(accountTransfer.getSource().getIbanNumber());
        }

        if (Objects.nonNull(accountTransfer.getDestination())) {
            dto.setDestination(accountTransfer.getDestination().getIbanNumber());
        }
        if (Objects.nonNull(accountTransfer.getTimeStamp())) {
            dto.setExecutionTime(accountTransfer.getTimeStamp());
        }

        return dto;
    }
}
