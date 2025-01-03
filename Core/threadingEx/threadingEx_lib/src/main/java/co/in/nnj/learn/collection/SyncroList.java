package co.in.nnj.learn.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SyncroList {
    public static void main(final String[] args) {

        //-- final List<Integer> numList = new ArrayList<>();
        final List<Integer> numList = Collections.synchronizedList(new ArrayList<>());

        final Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    numList.add(i);
                }
            }

        });

        final Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    numList.add(i);
                }
            }

        });
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Size = " + numList.size() + ", expected size = 2000");

    }
}
