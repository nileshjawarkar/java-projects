package co.in.nnj.learn.jee.persi.rel;

import java.io.Serializable;
import java.util.UUID;

import jakarta.json.bind.annotation.JsonbTypeAdapter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

@Entity
@Table(name = "items")
@NamedQuery(name = Item.CART_ITEMS, query = "select c from Item c where c.cart = :Cart") 
public class Item implements Serializable {
    private static final long serialVersionUID = 11111L;
    public static final String CART_ITEMS = "Cart.Items";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonbTypeAdapter(JsonUUIDAdapter.class)
    private UUID id;

    @Column(name = "title")
    private String title;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    public Cart getCart() {
        return cart;
    }
    public void setCart(final Cart cart) {
        this.cart = cart;
    }

    public UUID getId() {
        return id;
    }
    public void setId(final UUID id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(final String title) {
        this.title = title;
    }
}
