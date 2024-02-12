package com.axreng.enums;

public enum SearchStatus {

    ACTIVE("active"),
    DONE("done");

    private final String status;

    SearchStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return this.status;
    }
}