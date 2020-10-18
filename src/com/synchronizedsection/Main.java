package com.synchronizedsection;

public class Main {
    /* lock method of synchronizing */
    public static void main(String[] args) throws InterruptedException {
        CommonCounter sharedObject = new CommonCounter();
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10000; i++)
                    sharedObject.increment();
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10000; i++)
                    sharedObject.decrement();
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println(String.format("Final count is %d", sharedObject.getCount()));

    }

    public static class CommonCounter {
        int count;
        Object lock = new Object();

        CommonCounter() {
            count = 0;
        }

        /*Advantage of lock method is that it doesn't put a lock on whole object, just locks the section enclosed
        Also the locks are reentrant explaination
        http://wiserhawk.blogspot.com/2016/05/what-is-reentrant-synchronization-or.html
        */
        public void increment() {
            synchronized (this.lock) {
                count++;
            }

        }

        public void decrement() {
            synchronized (this.lock) {
                count--;
            }

        }

        public int getCount() {
            synchronized (this.lock) {
                return count;
            }
        }
    }
}

