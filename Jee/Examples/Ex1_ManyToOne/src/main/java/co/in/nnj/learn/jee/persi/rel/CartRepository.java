package co.in.nnj.learn.jee.persi.rel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class CartRepository {
    @PersistenceContext
    EntityManager em;

    public Cart createCart(final Cart cart) {
        em.persist(cart);
        return cart;
    }

    public Optional<Cart> findCart(final UUID id) {
        return Optional.ofNullable(em.find(Cart.class, id));
    }

    public Item addItemToCart(final UUID cartID, final Item item) {
        return findCart(cartID).map(cart -> {
            cart.getItems().add(item);
            item.setCart(cart);
            em.persist(item);
            return item;
        }).orElseThrow();
    }

    public Optional<Item> findItem(final UUID id) {
        return Optional.ofNullable(em.find(Item.class, id));
    }

    public List<Item> getItemsInCart(final UUID cartId) {
        final Cart cart = new Cart();
        cart.setId(cartId);
        return em.createNamedQuery(Item.CART_ITEMS, Item.class).setParameter("Cart", cart).getResultList();
    }

    public Cart updateCart(final Cart cart) {
        return em.merge(cart);
    }
}
