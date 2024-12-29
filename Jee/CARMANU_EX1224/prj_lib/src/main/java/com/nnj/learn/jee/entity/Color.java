package com.nnj.learn.jee.entity;

public enum Color {
    BLACK, RED, GREY;

    public static Color toEnum(final String value) {
        if(value != null && !"".equals(value)) {
            try {
                return Color.valueOf(value.toUpperCase());
            } catch( final Exception e) {
            }
        }
        return null;
    }
}

