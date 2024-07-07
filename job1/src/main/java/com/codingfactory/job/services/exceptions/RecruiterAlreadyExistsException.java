package com.codingfactory.job.services.exceptions;

import java.io.Serial;

public class RecruiterAlreadyExistsException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    public RecruiterAlreadyExistsException(Class<?> entityClass, String username) {
        super("Entity " + entityClass.getSimpleName() + " with username " + username + " already exists.");
    }
}
