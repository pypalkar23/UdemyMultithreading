package com.capabilitiesdebugging;

import java.time.LocalDateTime;
import java.util.Random;

public class OwnTry
{
	public static final int THREADS = 5;

	public static final int MIN = 1;

	public static final int MAX = 5;

	public static void main(String[] args)
	{
		Random random = new Random();

		for (int i = 0; i < THREADS; i++)
		{
			//sleep time is  between 1 to 5 seconds;
			int sleepTime = (random.nextInt((MAX - MIN) + 1) + MIN) * 1000;
			Thread t = new Thread(new RunnableExpt(i, sleepTime));
			t.start();
		}
	}

	public static class RunnableExpt implements Runnable
	{
		Random random;

		int sleepTime;

		public static final int iterations = 10;

		String threadName;

		public RunnableExpt(int n, int sleepTime)
		{
			this.random = new Random();
			this.sleepTime = sleepTime;//random.nextInt(3) * 10;
			this.threadName = this.getClass().getSimpleName() + "_" + n;
			System.out.println(String.format("The thread %s is sleeping for %d", threadName, sleepTime));
		}

		@Override
		public void run()
		{
			for (int i = 0; i <= iterations; i++)
			{
				try
				{
					Thread.sleep(this.sleepTime);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				System.out.println(String.format("Thread %s is running at %d:%d:%d", this.threadName, LocalDateTime.now().getMinute(), LocalDateTime.now().getSecond(),
						LocalDateTime.now().getNano()));
			}
		}
	}
}
