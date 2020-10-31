package com.atomicvolatality;

import java.util.Random;

class MinMaxMetrics{
    private volatile long min;
    private volatile long max;

    public MinMaxMetrics() {
        min = Long.MAX_VALUE;
        max = Long.MIN_VALUE;
    }

    /**
     * Adds a new sample to our metrics.
     */
    public void addSample(long newSample) {
        //another solution would be to make the method synchronized but it will create monitor on whole object
        //so locking a part of an operation is a better choice
        synchronized (this) {
            if (newSample < min)
                min = newSample;
            else if (newSample > max)
                max = newSample;
        }
    }

    /**
     * Returns the smallest sample we've seen so far.
     */
    public long getMin() {
        return min;
    }

    /**
     * Returns the biggest sample we've seen so far.
     */
    public long getMax() {
        // Add code here
        return max;
    }
}
class MinMaxExcercise {
    static class BusinessThread extends Thread {
        MinMaxMetrics minMaxMetrics;
        Random random;

        public BusinessThread(MinMaxMetrics minMaxMetrics) {
            this.minMaxMetrics = minMaxMetrics;
            random = new Random();
        }

        @Override
        public void run() {
            while (true) {
                long start = System.currentTimeMillis();
                try {
                    Thread.sleep(random.nextInt(10));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                long end = System.currentTimeMillis();
                this.minMaxMetrics.addSample(end - start);
            }
        }
    }

    static class MetricsPrinter extends Thread {
        MinMaxMetrics minMaxMetrics;
        Random random;

        public MetricsPrinter(MinMaxMetrics minMaxMetrics) {
            this.minMaxMetrics = minMaxMetrics;
            random = new Random();
        }

        @Override
        public void run() {
            while (true) {
                long start = System.currentTimeMillis();
                try {
                    Thread.sleep(random.nextInt(1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("<---------------------------------------------->");
                System.out.println(this.minMaxMetrics.getMax());
                System.out.println(this.minMaxMetrics.getMin());
                System.out.println("<---------------------------------------------->");
            }
        }
    }

    public static void main(String[] args) {
        MinMaxMetrics minMaxMetrics=new MinMaxMetrics();
        BusinessThread thread1 = new BusinessThread(minMaxMetrics);
        BusinessThread thread2 = new BusinessThread(minMaxMetrics);
        MetricsPrinter printer = new MetricsPrinter(minMaxMetrics);
        thread1.start();
        thread2.start();
        printer.start();
    }

}
