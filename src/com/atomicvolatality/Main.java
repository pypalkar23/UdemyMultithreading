package com.atomicvolatality;

import java.util.Random;

public class Main {
    static class Metrics {
        private long count;
        //long and double assign ops are not atomic in java
        private volatile double average;

        public Metrics() {
            count = 0;
            average = 0.0;
        }

        public synchronized void addSample(long sample) {
            double currentSum = average * count;
            count++;
            average = (currentSum + sample) / count;
        }

        public double getAverage() {
            return average;
        }
    }

    static class BusinessThread extends Thread {
        Metrics metrics;
        Random random;

        public BusinessThread(Metrics metrics) {
            this.metrics = metrics;
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
                this.metrics.addSample(end - start);
            }
        }
    }

    static class MetricsPrinter extends Thread {
        Metrics metrics;
        Random random;

        public MetricsPrinter(Metrics metrics) {
            this.metrics = metrics;
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

                System.out.println(this.metrics.getAverage());
            }
        }
    }

    public static void main(String[] args) {
        Metrics metrics = new Metrics();
        BusinessThread thread1 = new BusinessThread(metrics);
        BusinessThread thread2 = new BusinessThread(metrics);
        MetricsPrinter printer = new MetricsPrinter(metrics);
        thread1.start();
        thread2.start();
        printer.start();
    }
}
