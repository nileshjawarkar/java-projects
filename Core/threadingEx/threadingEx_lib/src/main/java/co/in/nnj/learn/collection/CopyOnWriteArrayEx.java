package co.in.nnj.learn.collection;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/* Advantage of CopyOnWriteArrayList over Syncronised arrayList -
 * 1) Read operation is also blocking in Syncronised arrayList, but its not in CopyOnWriteArrayList.
 * 2) CopyOnWriteArrayList makes copy underline data before write, so reading can continue without any issue as it can read underline data.
 */
public class CopyOnWriteArrayEx {

    public static class Updater implements Runnable {
        private final List<Integer> list;
        private final Random random;

        public Updater(final List<Integer> arrayList) {
            list = arrayList;
            random =  new Random();
        }

        @Override
        public void run() {
            while (true) {
                //-- Generate random index and value
                final int idx = random.nextInt(10);
                final int value = random.nextInt(10);
                if (idx < 10) {
                    list.set(idx, value);
                }
                System.out.println(list.toString());

                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (final InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(final String[] args) {
        final List<Integer> arrayList = new CopyOnWriteArrayList<>();
        arrayList.addAll(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

        new Thread(new Updater(arrayList)).start();
        new Thread(new Updater(arrayList)).start();

    }
}
