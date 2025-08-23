package co.in.nnj.learn.jee.persi.ms;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
//-- import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;

@Stateless
public class MessageRepository {
    @PersistenceContext
    private EntityManager em;

    public long addNew(final String payload) {
        final Message msg = new Message();
        msg.setPayload(payload);
        msg.setStatus("NEW");
        em.persist(msg);

        return msg.getId();
    }

    // -- @Transactional
    /*
     * public Message fetchAndMarkInWork() {
     * // Fetch one NEW message (for update to avoid race conditions)
     * final Message msg = em.createQuery(
     * "SELECT m FROM Message m WHERE m.status = :status", Message.class)
     * .setParameter("status", "NEW")
     * .setMaxResults(1)
     * .setLockMode(LockModeType.PESSIMISTIC_WRITE)
     * .getResultStream()
     * .findFirst()
     * .orElse(null);
     * 
     * if (msg != null) {
     * msg.setStatus("INWORK");
     * em.merge(msg);
     * }
     * return msg;
     * }
     */

    public Message fetchAndMarkInWork() {
        // Step 1: Find one NEW message (no locking)
        final Message msg = em.createQuery(
                "SELECT m FROM Message m WHERE m.status = :status", Message.class)
                .setParameter("status", "NEW")
                .setMaxResults(1)
                .getResultStream()
                .findFirst()
                .orElse(null);

        // Step 2: If found, set to INWORK and persist
        if (msg != null) {
            msg.setStatus("INWORK");
            em.merge(msg);
        }
        return msg;
    }

    // -- @Transactional
    public void delete(final Long msgId) {
        final Message msg = em.find(Message.class, msgId);
        if (msg != null) {
            em.remove(msg);
        }
    }
}
