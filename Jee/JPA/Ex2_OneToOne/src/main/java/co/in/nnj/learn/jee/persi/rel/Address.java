package co.in.nnj.learn.jee.persi.rel;

import java.util.UUID;

import jakarta.json.bind.annotation.JsonbTypeAdapter;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonbTypeAdapter(JsonUUIDAdapter.class)
    private UUID id;

    // Not needed and will lead to recursive loadinga with default JAXP serilization
    // User -> Address -> User -> .....
    //-- @OneToOne(mappedBy = "address")
    //-- private User user;

    private String street;
    private String city;
    private String state;
    private String country;

    public UUID getId() {
        return id;
    }
    public void setId(final UUID id) {
        this.id = id;
    }
    public String getStreet() {
        return street;
    }
    public void setStreet(final String strret) {
        this.street = strret;
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
    public void setCountry(final String contry) {
        this.country = contry;
    }
}
