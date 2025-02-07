package co.in.nnj.learn.basics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public class Ex9_ReadWriteLocks {
    private final HashMap<Integer, List<String>> productIdToReviews;
    ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    ReadLock readLock = readWriteLock.readLock();
    WriteLock writeLock = readWriteLock.writeLock();

    public Ex9_ReadWriteLocks() {
        this.productIdToReviews = new HashMap<>();
    }

    /**
     * Adds a product ID if not present
     */
    public void addProduct(final int productId) {
        writeLock.lock();
        try {
            if (!productIdToReviews.containsKey(productId)) {
                productIdToReviews.put(productId, new ArrayList<>());
            }
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Removes a product by ID if present
     */
    public void removeProduct(final int productId) {
        writeLock.lock();
        try {
            if (productIdToReviews.containsKey(productId)) {
                productIdToReviews.remove(productId);
            }
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Adds a new review to a product
     * 
     * @param productId - existing or new product ID
     * @param review    - text containing the product review
     */
    public void addProductReview(final int productId, final String review) {
        writeLock.lock();
        try {
            if (!productIdToReviews.containsKey(productId)) {
                productIdToReviews.put(productId, new ArrayList<>());
            }
            productIdToReviews.get(productId).add(review);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Returns all the reviews for a given product
     */
    public List<String> getAllProductReviews(final int productId) {
        readLock.lock();
        try {
            if (productIdToReviews.containsKey(productId)) {
                return Collections.unmodifiableList(productIdToReviews.get(productId));
            }
        } finally {
            readLock.unlock();
        }
        return Collections.emptyList();
    }

    /**
     * Returns the latest review for a product by product ID
     */
    public Optional<String> getLatestReview(final int productId) {
        readLock.lock();
        try {
            if (productIdToReviews.containsKey(productId) && !productIdToReviews.get(productId).isEmpty()) {
                final List<String> reviews = productIdToReviews.get(productId);
                return Optional.of(reviews.get(reviews.size() - 1));
            }
        } finally {
            readLock.unlock();
        }
        return Optional.empty();
    }

    /**
     * Returns all the product IDs that contain reviews
     */
    public Set<Integer> getAllProductIdsWithReviews() {
        readLock.lock();
        try {
            final Set<Integer> productsWithReviews = new HashSet<>();
            for (final Map.Entry<Integer, List<String>> productEntry : productIdToReviews.entrySet()) {
                if (!productEntry.getValue().isEmpty()) {
                    productsWithReviews.add(productEntry.getKey());
                }
            }
            return productsWithReviews;
        } finally {
            readLock.unlock();
        }
    }

    public static class Writter extends Thread {
        private final Ex9_ReadWriteLocks advLocks;
        private final int[] prdIds;

        public Writter(final Ex9_ReadWriteLocks advLocks, final int[] prdIds) {
            this.advLocks = advLocks;
            this.prdIds = prdIds;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    exec();
                    TimeUnit.SECONDS.sleep(1);
                } catch (final InterruptedException e) {
                    break;
                }
            }
        }

        private void exec() throws InterruptedException {
            for (final int prdId : prdIds) {
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
                final String review = "MSG" + (Math.random() * 100) + "#";
                advLocks.addProductReview(prdId, review.replace(".", "#"));
            }
        }
    }

    public static class Reader extends Thread {
        private final Ex9_ReadWriteLocks advLocks;
        private final int[] prdIds;

        public Reader(final Ex9_ReadWriteLocks advLocks, final int[] prdIds) {
            this.advLocks = advLocks;
            this.prdIds = prdIds;
        }

        private void exec() throws InterruptedException {
            for (final int prdId : prdIds) {
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
                final Optional<String> latestReview = advLocks.getLatestReview(prdId);
                if (latestReview.isPresent()) {
                    System.out.println("Review for prd = " + prdId + ", Review = " + latestReview.get());
                }
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    exec();
                    TimeUnit.SECONDS.sleep(1);
                } catch (final InterruptedException e) {
                    break;
                }
            }
        }
    }

    public static class Remover extends Thread {
        private final Ex9_ReadWriteLocks advLocks;
        private final int[] prdIds;

        public Remover(final Ex9_ReadWriteLocks advLocks, final int[] prdIds) {
            this.advLocks = advLocks;
            this.prdIds = prdIds;
        }

        private void exec() throws InterruptedException {
            for (final int prdId : prdIds) {
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
                advLocks.removeProduct(prdId);
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    exec();
                    TimeUnit.SECONDS.sleep(2);
                } catch (final InterruptedException e) {
                    break;
                }
            }
        }
    }

    public static void main(final String[] args) {
        final Ex9_ReadWriteLocks advLocks = new Ex9_ReadWriteLocks();
        final int[] prdIds = { 1, 2, 3, 4, 5 };
        final Writter writter = new Writter(advLocks, prdIds);
        final Reader reader = new Reader(advLocks, prdIds);
        final Remover remover = new Remover(advLocks, prdIds);
        writter.start();
        reader.start();
        remover.start();

        try {
            TimeUnit.SECONDS.sleep(20);
        } catch (final InterruptedException e) {
        }
        writter.interrupt();
        reader.interrupt();
        remover.interrupt();
    }
}
