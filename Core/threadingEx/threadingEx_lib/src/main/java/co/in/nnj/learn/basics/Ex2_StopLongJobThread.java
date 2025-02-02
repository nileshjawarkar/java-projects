package co.in.nnj.learn.basics;

import java.math.BigInteger;

public class Ex2_StopLongJobThread {

    public static class BigOps extends Thread {
        BigInteger num;
        BigInteger pow;

        public BigOps(final BigInteger num, final BigInteger pow) {
            this.num = num;
            this.pow = pow;
        }

        @Override
        public void run() {
            BigInteger result = BigInteger.ZERO;
            for (final BigInteger i = BigInteger.ZERO; i.compareTo(pow) != 0; i.add(BigInteger.ONE)) {
                //-- Need to add this check to stop the tread.
                //-- Otherwise even if it is interrupted, it will not stop
                if( Thread.currentThread().isInterrupted() ) {
                    result = BigInteger.ZERO;
                    System.out.println("Big job interrupted");
                    break;
                }
                result = result.multiply(num);    
            }
            System.out.println("Result = " + result);
        }
    }

    public static void main(final String[] args) throws InterruptedException {
        //-- Example 1: Here we use interrupt API to stop the thread.
        //-- Example 2: In this Example simple use of interrupt will not work.
        //-- Need modification run method to break the loop on calling the interrupt.
        final Thread ex = new BigOps(new BigInteger("11111"), new BigInteger("11111"));
        ex.start();
        Thread.sleep(5000);
        ex.interrupt();
    }
}
