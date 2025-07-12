package co.in.nnj.learn.jee.domain;

import java.util.UUID;

public record Department(String name, DepartmentType function, UUID id) {
    public Department(final String name, final DepartmentType function) {
        this(name, function, null);
    }
} 

