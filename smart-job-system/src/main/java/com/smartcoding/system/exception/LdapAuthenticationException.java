package com.smartcoding.system.exception;

import org.springframework.security.core.AuthenticationException;

public class LdapAuthenticationException extends AuthenticationException {

    public LdapAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public LdapAuthenticationException(String msg) {
        super(msg);
    }
}
