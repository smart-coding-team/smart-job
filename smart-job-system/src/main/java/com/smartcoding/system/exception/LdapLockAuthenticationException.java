package com.smartcoding.system.exception;

import org.springframework.security.core.AuthenticationException;

public class LdapLockAuthenticationException extends AuthenticationException {

    public LdapLockAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public LdapLockAuthenticationException(String msg) {
        super(msg);
    }
}
