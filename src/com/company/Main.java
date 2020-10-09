package com.company;

public class Main {

    public static void main(String[] args) throws InterruptedException {
	// write your code here
        Thread thread= new Thread(new Runnable(){
            @Override
            public void run() {
                //code that will run in our thread;
                /*System.out.println("We are now in a thread"+ Thread.currentThread());
                System.out.println("The thread priority is"+ Thread.currentThread().getPriority());*/
                throw new RuntimeException("Intentional Exception");
            }
        });

        thread.setName("Worker Thread");

        thread.setPriority(Thread.MAX_PRIORITY);
        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("Generating Critical Error in "+t.getName() +" error is "+e.getMessage());
            }
        });
        System.out.println("We are in thread"+ Thread.currentThread()+"before starting a new thread");
        thread.start();
        System.out.println("We are in thread"+ Thread.currentThread()+"after starting a new thread");

    }

}

