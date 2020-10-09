package com.capabilitiesdebugging;

public class Main
{
	public static void main(String[] args) throws InterruptedException
	{
		Thread thread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				System.out.println("We are now in thread" + Thread.currentThread().getName());
			}
		});

		thread.setName("Worker Thread");

		/*Can be used for freeing the resources in case of an exception that is not caught at all in
		 * any function thread was performing*/
		thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler()
		{
			@Override
			public void uncaughtException(Thread t, Throwable e)
			{
				System.out.println("Critical Error Happened in thread" + Thread.currentThread().getName());
			}
		});

		System.out.println("Before starting the thread --> Currently In the Thread" + Thread.currentThread().getName());
		thread.start();
		System.out.println("After starting the thread --> Currently In the Thread" + Thread.currentThread().getName());
		Thread.sleep(1000);
	}
}
