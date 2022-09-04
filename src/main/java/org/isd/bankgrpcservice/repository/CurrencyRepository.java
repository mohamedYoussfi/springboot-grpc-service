package org.isd.bankgrpcservice.repository;

import org.isd.bankgrpcservice.entities.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyRepository extends JpaRepository<Currency,Long> {
    Currency findByName(String name);
}
