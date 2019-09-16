package com.murray.financial.domain.repository;

import com.murray.financial.domain.entity.AccountTransfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountTransferJPARespository extends JpaRepository<AccountTransfer, Long>, AccountTransferJPACustomRepository {

}
