package com.murray.financial.adapter.converter;

import com.murray.financial.domain.entity.AccountTransfer;
import com.murray.financial.dtos.TransferDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts the AccountTransfer domain to a {@link TransferDTO}
 */
@Component
public class AccountTransferDomainToDto implements Converter<AccountTransfer, TransferDTO> {


    @Override
    public TransferDTO convert(AccountTransfer accountTransfer) {

        TransferDTO dto = new TransferDTO();
        dto.setExecutionTime(accountTransfer.getTimeStamp());
        dto.setDestination(accountTransfer.getDestination().getIbanNumber());
        dto.setSource(accountTransfer.getSource().getIbanNumber());
        dto.setDestination(accountTransfer.getDescription());
        dto.setAmount(accountTransfer.getAmount());
        dto.setId(accountTransfer.getId());
        dto.setStatus("COMPLETED");
        return null;
    }
}
