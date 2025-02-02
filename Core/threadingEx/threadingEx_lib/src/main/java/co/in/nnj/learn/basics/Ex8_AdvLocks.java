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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public class Ex8_AdvLocks {
    private final HashMap<Integer, List<String>> productIdToReviews;
    ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    ReadLock readLock = readWriteLock.readLock();
    WriteLock writeLock = readWriteLock.writeLock();

    public Ex8_AdvLocks() {
        this.productIdToReviews = new HashMap<>();
    }

    /**
     * Adds a product ID if not present
     */
    public void addProduct(final int productId) {
        final Lock lock = getLockForAddProduct();
        lock.lock();
        try {
            if (!productIdToReviews.containsKey(productId)) {
                productIdToReviews.put(productId, new ArrayList<>());
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Removes a product by ID if present
     */
    public void removeProduct(final int productId) {
        final Lock lock = getLockForRemoveProduct();
        lock.lock();
        try {
            if (productIdToReviews.containsKey(productId)) {
                productIdToReviews.remove(productId);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Adds a new review to a product
     * 
     * @param productId - existing or new product ID
     * @param review    - text containing the product review
     */
    public void addProductReview(final int productId, final String review) {
        final Lock lock = getLockForAddProductReview();
        lock.lock();
        try {
            if (!productIdToReviews.containsKey(productId)) {
                productIdToReviews.put(productId, new ArrayList<>());
            }
            productIdToReviews.get(productId).add(review);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Returns all the reviews for a given product
     */
    public List<String> getAllProductReviews(final int productId) {
        final Lock lock = getLockForGetAllProductReviews();
        lock.lock();
        try {
            if (productIdToReviews.containsKey(productId)) {
                return Collections.unmodifiableList(productIdToReviews.get(productId));
            }
        } finally {
            lock.unlock();
        }
        return Collections.emptyList();
    }

    /**
     * Returns the latest review for a product by product ID
     */
    public Optional<String> getLatestReview(final int productId) {
        final Lock lock = getLockForGetLatestReview();
        lock.lock();
        try {

            if (productIdToReviews.containsKey(productId) && !productIdToReviews.get(productId).isEmpty()) {
                final List<String> reviews = productIdToReviews.get(productId);
                return Optional.of(reviews.get(reviews.size() - 1));
            }
        } finally {
            lock.unlock();
        }
        return Optional.empty();
    }

    /**
     * Returns all the product IDs that contain reviews
     */
    public Set<Integer> getAllProductIdsWithReviews() {
        final Lock lock = getLockForGetAllProductIdsWithReviews();
        lock.lock();
        try {
            final Set<Integer> productsWithReviews = new HashSet<>();
            for (final Map.Entry<Integer, List<String>> productEntry : productIdToReviews.entrySet()) {
                if (!productEntry.getValue().isEmpty()) {
                    productsWithReviews.add(productEntry.getKey());
                }
            }
            return productsWithReviews;
        } finally {
            lock.unlock();
        }
    }

    Lock getLockForAddProduct() {
        return writeLock;
    }

    Lock getLockForRemoveProduct() {
        return writeLock;
    }

    Lock getLockForAddProductReview() {
        return writeLock;
    }

    Lock getLockForGetAllProductReviews() {
        return readLock;
    }

    Lock getLockForGetLatestReview() {
        return readLock;
    }

    Lock getLockForGetAllProductIdsWithReviews() {
        return readLock;
    }

    public static class Writter extends Thread {
        private final Ex8_AdvLocks advLocks;
        private final int[] prdIds;

        public Writter(final Ex8_AdvLocks advLocks, final int[] prdIds) {
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
                final String review = "Review - " + Math.random();
                advLocks.addProductReview(prdId, review);
            }
        }
    }

    public static class Reader extends Thread {
        private final Ex8_AdvLocks advLocks;
        private final int[] prdIds;

        public Reader(final Ex8_AdvLocks advLocks, final int[] prdIds) {
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
        private final Ex8_AdvLocks advLocks;
        private final int[] prdIds;

        public Remover(final Ex8_AdvLocks advLocks, final int[] prdIds) {
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
        final Ex8_AdvLocks advLocks = new Ex8_AdvLocks();
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
