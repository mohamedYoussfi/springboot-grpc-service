package org.isd.bankgrpcservice.repository;

import org.isd.bankgrpcservice.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,String> {
}
