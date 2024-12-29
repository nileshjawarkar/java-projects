package co.in.nnj.learn;

public class TaskPipeline {
    public static class Connector {
        Task current;
        String taskDesc = null;
        Connector next;

        public Connector(final Task current, final String desc) {
            this.current = current;
            this.next = null;
            this.taskDesc = desc;
        }

        public void setNext(final Connector next) {
            this.next = next;
        }

        public Connector next(final int i) {
            if(taskDesc != null && !"".equals(taskDesc)) {
                System.out.println("#Start# Task-" + i + ": " + taskDesc);
            }
            current.execute();
            if(taskDesc != null && !"".equals(taskDesc)) {
                System.out.println("#End# Task-" + i + ": " + taskDesc);
            }
            return next;
        }
    }

    private Connector start = null;
    private Connector end = null;
    private int taskCount = 0;

    public TaskPipeline add(final Task task, final String desc) {
        if (start == null) {
            start = new Connector(task, desc);
            end = start;
        } else {
            final Connector next = new Connector(task, desc);
            end.setNext(next);
            end = next;
        }
        return this;
    }

    private void next() {
        start = start.next(taskCount += 1);
    }

    public void execute() {
        end = null;
        while (start != null) {
            next();
        }
    }
}
