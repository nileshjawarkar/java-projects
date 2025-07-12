package co.in.nnj.learn.jee.persi.rel;

import java.util.Optional;
import java.util.UUID;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class UserRepository {

    @PersistenceContext
    EntityManager em;

    public User createUser(final User user) {
        em.persist(user);
        return user;
    }

    public Optional<User> findUser(final UUID id) {
        return Optional.ofNullable(em.find(User.class, id));
    }

    public boolean assignAddress(final UUID userId, final Address address) {
        return findUser(userId).map(user -> {
            address.setUser(user);
            user.setAddress(address);
            em.persist(user);
            return true;
        }).orElse(false);
    }
}
