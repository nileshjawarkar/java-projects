package co.in.nnj.learn;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TaskExecutor {
    private final List<WorkerThread> threads;
    private int lastAvailableIdx = -1;

    public TaskExecutor(final int poolSize) {
        threads = new ArrayList<>();
        final String name = "WorkerThread-";
        for (int i = 0; i < poolSize; i++) {
            threads.add(new WorkerThread(name + i));
        }
    }

    public boolean isAnyExecutorAvailable() {
        for (int i = 0; i < threads.size(); i++) {
            final WorkerThread curThread = threads.get(i);
            if (curThread != null && !curThread.isAlive()) {
                lastAvailableIdx = i;
                return true;
            }
        }
        return false;
    }

    public boolean isAllExecutorAvailable() {
        for (final WorkerThread workerThread : threads) {
            if (workerThread.isAlive()) {
                return false;
            }
        }
        return true;
    }

    public void executeTask(final Task task) {
        // -- If lastAvailableIdx thread is some how not available now,
        // -- may be user code didnt called isAnyAvailable method, in that
        // -- case calling it here to re-compute it.
        WorkerThread availableThread = threads.get(lastAvailableIdx);
        if (availableThread == null || availableThread.isAlive()) {
            if (!isAnyExecutorAvailable()) {
                return;
            }
            availableThread = threads.get(lastAvailableIdx);
        }

        if (availableThread != null && !availableThread.isAlive()) {
            availableThread.start(task);
        }
    }

    public void waitToCompleteAllTask() {
        while (!isAllExecutorAvailable()) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (final InterruptedException e) {
            }
        }
    }

    private static final class WorkerThread {
        private final String name;
        private Thread curThread = null;

        public WorkerThread(final String name) {
            this.name = name;
        }

        public void start(final Task task) {
            if (task != null) {
                curThread = new Thread(name) {
                    @Override
                    public void run() {
                        task.execute();
                    }
                };
                curThread.start();
            }
        }

        public boolean isAlive() {
            if (curThread != null) {
                return curThread.isAlive();
            }
            return false;
        }
    }

}
