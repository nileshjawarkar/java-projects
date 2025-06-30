package co.in.nnj.learn.jee.adapter.output.db.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "address")
public class AddressEntity extends BaseEntity {
    private String street;
    private String city;
    private String state;
    private String country;
    private String pin;
    private String landscape;

    public String getStreet() {
        return street;
    }

    public void setStreet(final String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(final String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(final String pin) {
        this.pin = pin;
    }

    public String getLandscape() {
        return landscape;
    }

    public void setLandscape(final String landscape) {
        this.landscape = landscape;
    }

    @Override
    public String toString() {
        return "AddressEntity{street=" + street + ", city=" + city + ", state=" + state + ", pin=" + pin
                + ", landscape=" + landscape + ", id=" + getId() + "}";
    }

}
