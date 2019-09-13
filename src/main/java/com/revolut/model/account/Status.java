package com.revolut.model.account;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.revolut.model.Money;

public class Status {

    private String code;
    private String operation;
    private Money transfer;

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private String details;

    public static Status ok(String operation, Money transfer) {
        return new Status(operation, transfer, "OK", "");
    }

    public static Status error(String operation, Money transfer, String details) {
        return new Status(operation, transfer, "ERROR", details);
    }

    Status(String operation, Money transfer, String code, String details) {
        this.operation = operation;
        this.transfer = transfer;
        this.code = code;
        this.details = details;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Money getTransfer() {
        return transfer;
    }

    public void setTransfer(Money transfer) {
        this.transfer = transfer;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
