package com.synchronizedfunctions;

public class Main
{
	/* Monitor method of synchronizing */
	public static void main(String[] args) throws InterruptedException
	{
		CommonCounter sharedObject = new CommonCounter();
		Thread thread1 = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				for (int i = 0; i < 10000; i++)
					sharedObject.increment();
			}
		});

		Thread thread2 = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
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

	public static class CommonCounter
	{
		int count;

		CommonCounter()
		{
			count = 0;
		}

		/*makes access to method synchronized. Only drawback is using synchronized over a method makes
		whole object atomic. So at a time only one thread can access the object even if it is not interested in method
		that is not used by any other methods*/
		public synchronized void increment()
		{
			count++;
		}

		public synchronized void decrement()
		{
			count--;
		}

		public int getCount()
		{
			return count;
		}
	}
}
