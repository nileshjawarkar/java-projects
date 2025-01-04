package co.in.nnj.learn.collection;

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
public class BlockingQueue_PriorityQueueEx {
    private static class Order implements Comparable<Order> {
        private double amount;
        private String customer_name;

        public Order(String customer_name, double amount) {
            this.customer_name = customer_name;
            this.amount = amount;
        }

        @Override
        public int compareTo(Order other) {
            return Double.compare(other.amount, amount);
        }

        @Override
        public String toString() {
            return "Order{Amount=" + amount + ", CustomerName=" + customer_name + "}";
        }
    }

    private static class Waiter implements Runnable {
        BlockingQueue<Order> queue;

        public Waiter(BlockingQueue<Order> queue) {
            this.queue = queue;
        }

        public void takeOrder(String name, double amount) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Order o = new Order(name, amount);
            queue.add(o);
        }

        @Override
        public void run() {
            int i = 1;
            Random random = new Random();
            while (true) {
                String name = "Customer-" + i;
                double amount = random.nextDouble(10000);
                takeOrder(name, amount);
                i += 1;
            }
        }
    }

    private static class Hotel implements Runnable {
        BlockingQueue<Order> queue;

        public Hotel(BlockingQueue<Order> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            while (true) {
                processOrder();
            }
        }

        private void processOrder() {
            Order o = queue.poll();
            System.out.println("Processing - " + o);
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Ready - " + o);
        }
    }

    public static void main(String[] args) {
        BlockingQueue<Order> queue = new PriorityBlockingQueue<>();
        Waiter waiter = new Waiter(queue);
        Hotel hotel = new Hotel(queue);

        waiter.takeOrder("Pradnya", 500);
        waiter.takeOrder("Manu", 10000);
        waiter.takeOrder("Shrikant", 2000);
        waiter.takeOrder("Tanu", 5000);

        new Thread(hotel).start();
        new Thread(waiter).start();
    }
}
