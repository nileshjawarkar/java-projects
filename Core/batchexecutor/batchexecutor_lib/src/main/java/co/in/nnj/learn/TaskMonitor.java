package co.in.nnj.learn;

import java.util.HashMap;
import java.util.Map;

public class TaskMonitor {

    private static TaskMonitor taskMonitor = null;

    private TaskMonitor() {
    }

    private static synchronized void create() {
        if (taskMonitor == null) {
            taskMonitor = new TaskMonitor();
        }
    }

    public static TaskMonitor get() {
        if (taskMonitor == null) {
            create();
        }
        return taskMonitor;
    }

    private int taskCount = 0;
    private final Map<String, String> jobList = new HashMap<>();

    public synchronized void startJob(final String threadName, final String jobDetails) {
        if (!jobList.containsKey(threadName)) {
            jobList.put(threadName, jobDetails);
        }
    }

    public synchronized void endJob(final String threadName) {
        if (jobList.containsKey(threadName)) {
            jobList.remove(threadName);
            taskCount += 1;
        }
    }

    public int getCompletedJobCount() {
        return taskCount;
    }
}
