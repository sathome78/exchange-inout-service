package com.exrates.inout.domain.enums.invoice;


import com.exrates.inout.exceptions.InvoiceActionIsProhibitedForCurrencyPermissionOperationException;
import com.exrates.inout.exceptions.InvoiceActionIsProhibitedForNotHolderException;
import com.exrates.inout.exceptions.UnsupportedInvoiceStatusForActionException;
import com.exrates.inout.exceptions.UnsupportedWithdrawRequestStatusIdException;
import com.exrates.inout.exceptions.UnsupportedWithdrawRequestStatusNameException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.exrates.inout.domain.enums.invoice.InvoiceActionTypeEnum.*;

//@Log4j2
public enum TransferStatusEnum implements InvoiceStatus {
    CREATED_USER(1) {
        @Override
        public void initSchema(Map<InvoiceActionTypeEnum, InvoiceStatus> schemaMap) {
            schemaMap.put(POSTPONE, POSTPONED_AS_VOUCHER);
            schemaMap.put(POST, POSTED);
        }
    },
    POSTED(2) {
        @Override
        public void initSchema(Map<InvoiceActionTypeEnum, InvoiceStatus> schemaMap) {
        }
    },
    REVOKED_USER(3) {
        @Override
        public void initSchema(Map<InvoiceActionTypeEnum, InvoiceStatus> schemaMap) {
        }
    },
    REVOKED_ADMIN(5) {
        @Override
        public void initSchema(Map<InvoiceActionTypeEnum, InvoiceStatus> schemaMap) {
        }
    },
    POSTPONED_AS_VOUCHER(4) {
        @Override
        public void initSchema(Map<InvoiceActionTypeEnum, InvoiceStatus> schemaMap) {
            schemaMap.put(REVOKE, REVOKED_USER);
            schemaMap.put(REVOKE_ADMIN, REVOKED_ADMIN);
            schemaMap.put(PRESENT_VOUCHER, POSTED);
        }
    };

    final private Map<InvoiceActionTypeEnum, InvoiceStatus> schemaMap = new HashMap<>();
    private static final Logger log = LogManager.getLogger(WithdrawStatusEnum.class);

    @Override
    public InvoiceStatus nextState(InvoiceActionTypeEnum action) {
        action.checkRestrictParamNeeded();
        return nextState(schemaMap, action)
                .orElseThrow(() -> new UnsupportedInvoiceStatusForActionException(String.format("current state: %s action: %s", this.name(), action.name())));
    }

    @Override
    public InvoiceStatus nextState(InvoiceActionTypeEnum action, InvoiceActionParamsValue paramsValue) {
        try {
            action.checkAvailabilityTheActionForParamsValue(paramsValue);
        } catch (InvoiceActionIsProhibitedForNotHolderException e) {
            throw new InvoiceActionIsProhibitedForNotHolderException(String.format("current status: %s action: %s", this.name(), action.name()));
        } catch (InvoiceActionIsProhibitedForCurrencyPermissionOperationException e) {
            throw new InvoiceActionIsProhibitedForCurrencyPermissionOperationException(String.format("current status: %s action: %s permittedOperation: %s", this.name(), action.name(), paramsValue.getPermittedOperation().name()));
        } catch (Exception e) {
            throw e;
        }
        return nextState(schemaMap, action)
                .orElseThrow(() -> new UnsupportedInvoiceStatusForActionException(String.format("current state: %s action: %s", this.name(), action.name())));
    }

    @Override
    public Boolean availableForAction(InvoiceActionTypeEnum action) {
        return availableForAction(schemaMap, action);
    }

    static {
        for (TransferStatusEnum status : TransferStatusEnum.class.getEnumConstants()) {
            status.initSchema(status.schemaMap);
        }
        /*check schemaMap*/
        getBeginState();
    }

    public static List<InvoiceStatus> getAvailableForActionStatusesList(InvoiceActionTypeEnum action) {
        return Arrays.stream(TransferStatusEnum.class.getEnumConstants())
                .filter(e -> e.availableForAction(action))
                .collect(Collectors.toList());
    }

    public Set<InvoiceActionTypeEnum> getAvailableActionList(InvoiceActionParamsValue paramsValue) {
        return schemaMap.keySet().stream()
                .filter(e -> e.isMatchesTheParamsValue(paramsValue))
                .collect(Collectors.toSet());
    }

    /**/

    public static TransferStatusEnum convert(int id) {
        return Arrays.stream(TransferStatusEnum.class.getEnumConstants())
                .filter(e -> e.code == id)
                .findAny()
                .orElseThrow(() -> new UnsupportedWithdrawRequestStatusIdException(String.valueOf(id)));
    }

    public static TransferStatusEnum convert(String name) {
        return Arrays.stream(TransferStatusEnum.class.getEnumConstants())
                .filter(e -> e.name().equals(name))
                .findAny()
                .orElseThrow(() -> new UnsupportedWithdrawRequestStatusNameException(name));
    }

    public static InvoiceStatus getBeginState() {
        Set<InvoiceStatus> allNodesSet = collectAllSchemaMapNodesSet();
        List<InvoiceStatus> candidateList = Arrays.stream(TransferStatusEnum.class.getEnumConstants())
                .filter(e -> !allNodesSet.contains(e))
                .collect(Collectors.toList());
        if (candidateList.size() == 0) {
            log.fatal("begin state not found");
            throw new AssertionError();
        }
        if (candidateList.size() > 1) {
            log.fatal("more than single begin state found: " + candidateList);
            throw new AssertionError();
        }
        return candidateList.get(0);
    }

    public Boolean isEndStatus() {
        return schemaMap.isEmpty();
    }

    public Boolean isSuccessEndStatus() {
        Map<InvoiceActionTypeEnum, InvoiceStatus> schema = new HashMap<>();
        Arrays.stream(TransferStatusEnum.class.getEnumConstants())
                .forEach(e -> schema.putAll(e.schemaMap));
        return schema.entrySet().stream()
                .filter(e -> e.getValue() == this)
                .filter(e -> e.getKey().isLeadsToSuccessFinalState())
                .findAny()
                .isPresent();
    }

    private static Set<InvoiceStatus> collectAllSchemaMapNodesSet() {
        Set<InvoiceStatus> result = new HashSet<>();
        Arrays.stream(TransferStatusEnum.class.getEnumConstants())
                .forEach(e -> result.addAll(e.schemaMap.values()));
        return result;
    }

    private Integer code;

    TransferStatusEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public InvoiceActionTypeEnum getStartAction(Boolean isVoucher) {
        if (isVoucher) {
            return POSTPONE;
        } else {
            return POST;
        }
    }

}

