package com.nnj.learn.jee.entity;

public enum EngineType {
    PETROL, DIESEL, ELECTRIC;

    public static EngineType toEnum(final String value) {
        if(value != null && !"".equals(value)) {
            try {
                return EngineType.valueOf(value.toUpperCase());
            } catch( final Exception e) {
            }
        }
        return null;
    }
}

