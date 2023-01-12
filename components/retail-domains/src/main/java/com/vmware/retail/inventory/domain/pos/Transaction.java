package com.vmware.retail.inventory.domain.pos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    private String id;
    private POSTransaction posTransaction;
}
