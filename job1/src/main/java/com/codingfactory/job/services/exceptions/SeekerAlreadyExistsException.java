package com.codingfactory.job.services.exceptions;

import java.io.Serial;

public class SeekerAlreadyExistsException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    public SeekerAlreadyExistsException(Class<?> entityClass, String username) {
        super("Entity " + entityClass.getSimpleName() + " with username " + username + " already exists.");
    }
}