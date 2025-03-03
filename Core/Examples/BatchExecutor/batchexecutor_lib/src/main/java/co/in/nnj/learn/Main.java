package co.in.nnj.learn;

import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(final String[] args) {
        final IteratorSchedular schedular = new IteratorSchedular(4, 2);
        final TaskPipeline pipeline = new TaskPipeline();
        pipeline.add(new Task() {
            @Override
            public void execute() {
                try {
                    TimeUnit.SECONDS.sleep(2);
                    System.out.println("Init done");
                } catch (final InterruptedException e) {
                }
            }
        }, "Init" ).add(new Task() {
            @Override
            public void execute() {
                schedular.add(2, new Task() {
                    private final TaskMonitor monitor = TaskMonitor.get();
                    @Override
                    public void execute() {
                        System.out.println("\n# Completed - " + monitor.getCompletedJobCount());
                    }
                });
                schedular.start();
            }
        }, "Setup schedular").add(new Task() {
            @Override
            public void execute() {
                final int numThreads = 10;
                final int numJobs = 100;
                final TaskExecutor texe = new TaskExecutor(numThreads);
                final long st = System.currentTimeMillis();
                int taskNumber = 1;
                while (taskNumber <= numJobs) {
                    if (texe.isAnyExecutorAvailable()) {
                        final String taskName = ("Task-" + taskNumber);
                        texe.executeTask(new Task() {
                            private final String myName = taskName;
                            private final TaskMonitor taskMonitor = TaskMonitor.get();

                            @Override
                            public void execute() {
                                try {
                                    final String name = Thread.currentThread().getName();
                                    taskMonitor.startJob(name, myName);
                                    TimeUnit.SECONDS.sleep(1);
                                    System.out.println(name + "= " + myName);
                                    taskMonitor.endJob(name);
                                } catch (final InterruptedException e) {
                                }
                            }
                        });
                        taskNumber += 1;
                    } else {
                        try {
                            TimeUnit.MILLISECONDS.sleep(200);
                        } catch (final InterruptedException e) {
                        }
                    }
                }
                texe.waitToCompleteAllTask();
                final long et = System.currentTimeMillis();
                System.out.println("Completed " + numJobs + " job using " + numThreads + " threads in "
                        + TimeUnit.MILLISECONDS.toSeconds(et - st) + " Seconds");
                schedular.stop();
            }
        }, "ExecuteTasks").execute();
    }
}
