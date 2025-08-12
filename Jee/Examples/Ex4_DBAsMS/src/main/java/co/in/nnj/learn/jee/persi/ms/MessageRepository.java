package co.in.nnj.learn.jee.persi.ms;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;

@Stateless
public class MessageRepository {
    @PersistenceContext
    private EntityManager em;

    public void addNew(final String payload) {
        final Message msg = new Message();
        msg.setPayload(payload);
        msg.setStatus("NEW");
        em.persist(msg);
    }

    // -- @Transactional
    public Message fetchAndMarkInWork() {
        // Fetch one NEW message (for update to avoid race conditions)
        final Message msg = em.createQuery(
                "SELECT m FROM Message m WHERE m.status = :status", Message.class)
                .setParameter("status", "NEW")
                .setMaxResults(1)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .getResultStream()
                .findFirst()
                .orElse(null);

        if (msg != null) {
            msg.setStatus("INWORK");
            em.merge(msg);
        }
        return msg;
    }

    // -- @Transactional
    public void delete(final Long msgId) {
        Message msg = em.find(Message.class, msgId);
        if (msg != null) {
            em.remove(msg);
        }
    }
}
