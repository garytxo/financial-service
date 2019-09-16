package com.murray.financial.adapter.converter;

import com.murray.financial.domain.entity.BankAccount;
import com.murray.financial.dtos.BankAccountDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Converts a {@link BankAccount}  to a {@link BankAccountDTO}
 */
@Component
public class BankAccountDomainToDTO implements Converter<BankAccount, BankAccountDTO> {

    @Override
    public BankAccountDTO convert(BankAccount bankAccount) {


        BankAccountDTO dto = new BankAccountDTO();

        dto.setBalance(bankAccount.getBalance());
        dto.setIbanNumber(bankAccount.getIbanNumber());
        dto.setCurrency(bankAccount.getCurrency().name());
        dto.setOpenedOn(bankAccount.getOpenedOn());
        dto.setStatus(bankAccount.getStatus().name());

        return dto;
    }
}
