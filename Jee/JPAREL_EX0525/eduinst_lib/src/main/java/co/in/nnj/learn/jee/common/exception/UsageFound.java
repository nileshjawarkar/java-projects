package co.in.nnj.learn.jee.common.exception;

import jakarta.ejb.ApplicationException;

@ApplicationException
public class UsageFound extends RuntimeException {
    public UsageFound(final String message) {
        super(message);
    }
}
