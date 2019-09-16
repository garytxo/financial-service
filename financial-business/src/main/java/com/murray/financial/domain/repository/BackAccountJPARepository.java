package com.murray.financial.domain.repository;

import com.murray.financial.domain.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BackAccountJPARepository extends JpaRepository<BankAccount, Long>, BankAccountJPACustomRepository {


    BankAccount findFirstByIbanNumberEquals(final String ibanNumber);
}
