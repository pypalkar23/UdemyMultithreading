package com.SemaphoreOwnTry;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class SemaphoreSample
{
	private static final int MAX_THREADS = 3;

	private static Semaphore barrierSemaphore = new Semaphore(0);

	private static ReentrantLock lock = new ReentrantLock();

	public static void main(String[] args)
	{
		Barrier barrier = new Barrier();
		for (int i = 1; i <= MAX_THREADS; i++)
		{
			Thread thread = new Thread(new ThreadRunners(i, barrier));
			thread.start();
		}

	}

	private static class Barrier
	{
		private int counter = 0;

		private boolean isLastWorker;

		public void work(int i)
		{
			lock.lock();
			counter++;

			try
			{
				if (counter == MAX_THREADS)
				{
					isLastWorker = true;
				}
			}
			finally
			{
				lock.unlock();
			}

			if (isLastWorker)
			{
				System.out.println("Waiting for permits "+ barrierSemaphore.getQueueLength());
				barrierSemaphore.release(MAX_THREADS - 1);
				System.out.println("permit released "+ barrierSemaphore.availablePermits());
			}
			else
			{
				try
				{
					System.out.println("Thread " +i +" trying to get permit");
					barrierSemaphore.acquire();
					System.out.println("Thread " +i +" got permit "+ barrierSemaphore.getQueueLength());
				}
				catch (InterruptedException e)
				{

				}
			}

		}

	}

	private static class ThreadRunners implements Runnable
	{
		int threadId;

		Barrier barrier;

		public ThreadRunners(int threadId, Barrier barrier)
		{
			this.threadId = threadId;
			this.barrier = barrier;
		}

		@Override
		public void run()
		{
			System.out.println("Thread " + threadId + " Starting its work");
			barrier.work(threadId);
			System.out.println("Thread " + threadId + " Finishing its work");
		}
	}
}

