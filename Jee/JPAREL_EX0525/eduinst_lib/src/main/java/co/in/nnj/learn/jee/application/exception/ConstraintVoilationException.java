package co.in.nnj.learn.jee.application.exception;

import jakarta.ejb.ApplicationException;

@ApplicationException
public class ConstraintVoilationException extends RuntimeException {
    static final long serialVersionUID = 2123L;
    public ConstraintVoilationException(final String message) {
        super("Error - Constraint voilated. " + message);
    }
}
