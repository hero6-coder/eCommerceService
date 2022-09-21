package com.ecommerce.shopping.exception;

public enum StatusCode {
    SUCCESS(1), NOT_FOUND(2), ENTITY_EXISTED(3), USER_NAME_OR_PASSWORD_INCORRECT(4), UNAUTHORIZED(5);
    private int value;

    StatusCode(int value) {
        this.value = value;
    }
}
