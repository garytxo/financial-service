package com.murray.financial.adapter.converter;

import com.murray.financial.domain.repository.query.BankAccountResult;
import com.murray.financial.dtos.BankAccountDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Converts the BankAccountResult entity model to it's BankAccountDTO
 */
@Component
public class BankAccountResultToDTO implements Converter<BankAccountResult, BankAccountDTO> {

    @Override
    public BankAccountDTO convert(BankAccountResult result) {

        BankAccountDTO dto = new BankAccountDTO();

        dto.setStatus(result.status().name());

        if (Objects.nonNull(result.openedOn())) {
            dto.setOpenedOn(result.openedOn());
        }
        dto.setCurrency(result.currency().name());
        dto.setIbanNumber(result.ibanNumber());
        dto.setBalance(result.balance());


        return dto;
    }
}
