package co.in.nnj.learn.util;

public class TimeLogger {
    long st = -1;
    String tag = null;

    public void init(final String tag) {
        st = System.currentTimeMillis();
        this.tag = tag;
    }

    public void logTimeDiff() {
        if(st == -1) return;
        final long et = System.currentTimeMillis();        
        final long tt = et - st;
        System.out.println("[" + tag + "] Total time = " + tt + "MS");
    }
}
