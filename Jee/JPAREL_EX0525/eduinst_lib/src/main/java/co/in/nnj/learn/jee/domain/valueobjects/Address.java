
package co.in.nnj.learn.jee.domain.valueobjects;

import java.util.UUID;

public record Address(UUID id, String street, String city, String state, String country, String pin, String landscape) {
    public Address(final String street, final String city, final String state, final String country, final String pin, final String landscape) {
        this(null, street, city, state, country, pin, landscape);
    }
}
