package co.in.nnj.learn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class IteratorSchedular {
    private static class IteratorTasks {
        List<Task> tasks;
        public IteratorTasks() {
            tasks = new ArrayList<>();
        }
        public void add(final Task task) {
            tasks.add(task);
        }
    }

    private final Map<Integer, IteratorTasks> iterableTasks = new HashMap<>();
    private boolean running = true;
    private final int max_iterations;
    private final int sleep_per_iteration;

    public IteratorSchedular(final int max_iterations, final int sleep_per_iteration) {
        this.max_iterations = max_iterations;
        this.sleep_per_iteration = sleep_per_iteration;
    }

    public void add(final int iteration, final Task task) {
        IteratorTasks tasks = null;
        if (iterableTasks.containsKey(iteration)) {
            tasks = iterableTasks.get(iteration);
        } else {
            tasks = new IteratorTasks();
            iterableTasks.put(iteration, tasks);
        }
        if (tasks != null)
            tasks.add(task);
    }

    public void stop() {
        running = false;
    }

    private final Thread schedularThread = new Thread("IteratorSchedular") {
        @Override
        public void run() {
            while (running) {
                for (int i = 0; i < max_iterations; i++) {
                    if (iterableTasks.containsKey(i)) {
                        final IteratorTasks tasks = iterableTasks.get(i);
                        for (final Task task : tasks.tasks) {
                            task.execute();
                        }
                    }
                    try {
                        TimeUnit.SECONDS.sleep(sleep_per_iteration);
                    } catch (final InterruptedException e) {
                    }
                }
            }
        }
    };

    public void start() {
        schedularThread.start();
    }

    public boolean isAlive() {
        return schedularThread.isAlive();
    }
}
