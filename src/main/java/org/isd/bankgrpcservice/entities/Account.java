package org.isd.bankgrpcservice.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.isd.bankgrpcservice.enums.AccountStatus;
import org.isd.bankgrpcservice.enums.AccountType;

import javax.persistence.*;
import java.util.Date;
@Entity
@NoArgsConstructor @AllArgsConstructor @Data @Builder
public class Account {
    @Id
    private String id;
    private double balance;
    private long createAt;
    @Enumerated(EnumType.STRING)
    private AccountType type;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    @ManyToOne
    private Currency currency;
}
