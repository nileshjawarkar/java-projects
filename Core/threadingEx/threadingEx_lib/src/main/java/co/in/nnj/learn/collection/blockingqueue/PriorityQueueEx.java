package co.in.nnj.learn.collection.blockingqueue;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

/* Priority queue sort the queue element based on the ouput of the
 * compair method. This sorting basically govern the read output.
 *
 * Here we take example of Hotel and Orders placed by customer.
 * Consider a case, where hotel decided to process those order first, whos
 * amount is more than the other.
 *
 * It means, hotel will process order with higher amount first.
 */
public class PriorityQueueEx {
    private static class Order implements Comparable<Order> {
        private final double amount;
        private final String customer_name;

        public Order(final String customer_name, final double amount) {
            this.customer_name = customer_name;
            this.amount = amount;
        }

        @Override
        public int compareTo(final Order other) {
            return Double.compare(other.amount, amount);
        }

        @Override
        public String toString() {
            return "Order{Amount=" + amount + ", CustomerName=" + customer_name + "}";
        }
    }

    private static class Waiter implements Runnable {
        BlockingQueue<Order> queue;

        public Waiter(final BlockingQueue<Order> queue) {
            this.queue = queue;
        }

        public void takeOrder(final String name, final double amount) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }

            final Order o = new Order(name, amount);
            queue.add(o);
        }

        @Override
        public void run() {
            int i = 1;
            final Random random = new Random();
            while (true) {
                final String name = "Customer-" + i;
                final double amount = random.nextDouble(10000);
                takeOrder(name, amount);
                i += 1;
            }
        }
    }

    private static class Hotel implements Runnable {
        BlockingQueue<Order> queue;

        public Hotel(final BlockingQueue<Order> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            while (true) {
                processOrder();
            }
        }

        private void processOrder() {
            final Order o = queue.poll();
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Order ready - " + o);
        }
    }

    public static void main(final String[] args) {
        final BlockingQueue<Order> queue = new PriorityBlockingQueue<>();
        final Waiter waiter = new Waiter(queue);
        final Hotel hotel = new Hotel(queue);

        waiter.takeOrder("Pradnya", 6000);
        waiter.takeOrder("Shrikant", 7000);
        waiter.takeOrder("Tanu", 8000);
        waiter.takeOrder("Manu", 10000);

        new Thread(waiter).start();
        new Thread(hotel).start();
    }
}
