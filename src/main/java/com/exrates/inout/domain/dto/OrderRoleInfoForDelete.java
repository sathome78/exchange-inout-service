package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.enums.OrderStatus;
import com.exrates.inout.domain.enums.UserRole;
import lombok.AllArgsConstructor;

import static com.exrates.inout.domain.enums.UserRole.BOT_TRADER;

@AllArgsConstructor
public class OrderRoleInfoForDelete {
    private OrderStatus status;
    private UserRole creatorRole;
    private UserRole acceptorRole;
    private int transactionsCount;


    public boolean mayDeleteWithoutProcessingTransactions() {
        return status == OrderStatus.CLOSED && creatorRole == BOT_TRADER && acceptorRole == BOT_TRADER && transactionsCount == 0;
    }
}
