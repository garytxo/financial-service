package com.murray.financial.adapter.converter;

import com.murray.financial.domain.entity.BankAccount;
import com.murray.financial.domain.enums.AccountCurrency;
import com.murray.financial.domain.enums.AccountStatus;
import com.murray.financial.dtos.BankAccountDTO;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * Converts a {@link BankAccountDTO} to a {@link BankAccount}
 */
@Component
public class BankAccountDTOtoDomain implements Converter<BankAccountDTO, BankAccount> {


    @Override
    public BankAccount convert(BankAccountDTO dto) {

        BankAccount bankAccount = new BankAccount();

        bankAccount.setIbanNumber(dto.getIbanNumber());

        if (!StringUtils.isEmpty(dto.getStatus())) {
            bankAccount.setStatus(AccountStatus.toStatus(dto.getStatus()));
        }
        if (!StringUtils.isEmpty(dto.getCurrency())) {
            bankAccount.setCurrency(AccountCurrency.toCurrency(dto.getCurrency()));
        }

        if (Objects.nonNull(dto.getOpenedOn())) {
            bankAccount.setOpenedOn(dto.getOpenedOn());
        }


        return bankAccount;
    }
}
