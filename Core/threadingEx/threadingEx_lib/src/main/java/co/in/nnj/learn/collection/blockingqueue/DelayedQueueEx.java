package co.in.nnj.learn.collection.blockingqueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/* DelayedQueue is another impl of BlockingQueue.
 * It is used to consume objects after some delayed time/elapse time.
 * Element added to the queue will be available for access only after when delay time is crossed.
 * How queue knows it? By using getDelay method, which return remainig  time.
 */
public class DelayedQueueEx {

    public static class DelayedElement implements Delayed {
        private final long duration;
        private final String message;

        public DelayedElement(final long duration, final String message) {
            this.duration = System.currentTimeMillis() + duration;
            this.message = message;
        }

        @Override
        public int compareTo(final Delayed other) {
            final long otherDuration = ((DelayedElement) other).duration;
            if (duration < otherDuration) {
                return -1;
            } else if (duration > otherDuration) {
                return 1;
            }
            return 0;
        }

        //-- This return remaining delay time. 
        //-- This is why we need to substract current time.
        @Override
        public long getDelay(final TimeUnit unit) {
            return unit.convert(duration - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        public long getDuration() {
            return duration;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public String toString() {
            return "DelayedElement{duration=" + duration + ", message=" + message + "}";
        }

    }

    public static void main(final String[] args) {
        final BlockingQueue<DelayedElement> queue = new DelayQueue<>();

        //-- Add the objects to the queue
        final Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                queue.add(new DelayedElement(2000, "Message-1"));
                queue.add(new DelayedElement(8000, "Message-2"));
                queue.add(new DelayedElement(5000, "Message-3"));
                queue.add(new DelayedElement(1500, "Message-4"));
            }
        });

        t1.start();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }

        //-- Removing the objects from the queue
        while (!queue.isEmpty()) {
            try {
                final DelayedElement elem = queue.take();
                System.out.println(elem);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
