package co.in.nnj.learn.jee.application.exception;

import jakarta.ejb.ApplicationException;

@ApplicationException
public class UsageFound extends RuntimeException {
    static final long serialVersionUID = 2134L;
    public UsageFound(final String message) {
        super(message);
    }
}
