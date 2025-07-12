package co.in.nnj.learn.jee.eduinst.boundry;

import jakarta.ejb.ApplicationException;

@ApplicationException
public class ConstraintVoilationException extends RuntimeException {
    public ConstraintVoilationException(final String message) {
        super(message);
    }
}

