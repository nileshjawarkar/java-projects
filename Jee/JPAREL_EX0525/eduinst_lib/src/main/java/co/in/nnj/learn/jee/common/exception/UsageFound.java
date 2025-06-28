package co.in.nnj.learn.jee.common.exception;

import jakarta.ejb.ApplicationException;

@ApplicationException
public class UsageFound extends RuntimeException {
    static final long serialVersionUID = 2134L;
    public UsageFound(final String message) {
        super(message);
    }
}
