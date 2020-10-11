package com.fundamentalscoordination.interrupt.handling;

import java.math.BigInteger;

public class Main
{
	public static void main(String[] args) throws InterruptedException
	{
     Thread thread = new Thread(new LongBlockingTask(new BigInteger("200000"),new BigInteger("200000")));
     thread.setName("LongBlockingTask");
     thread.start();
     Thread.sleep(1000);
     thread.interrupt();
     Thread thread1 = new Thread(new SleepingThread());
     thread1.start();
     thread1.interrupt();
	}

	public static class LongBlockingTask implements Runnable
	{
		private BigInteger base;

		private BigInteger power;

		public LongBlockingTask(BigInteger base, BigInteger power)
		{
			this.base = base;
			this.power = power;
		}



		public BigInteger pow(BigInteger base, BigInteger power)
		{
			BigInteger result = BigInteger.ONE;

			for (BigInteger i = BigInteger.ZERO; i.compareTo(power) != 0; i = i.add(BigInteger.ONE))
			{
				System.out.println(i);
				//If not for this block thread keeps on running even when interrupted
				if (Thread.currentThread().isInterrupted())
				{
					System.out.println("Prematurely ended");
					return BigInteger.ZERO;
				}
				result = result.multiply(base);
			}

			return result;
		}

		@Override
		public void run()
		{
			System.out.println(base + "*" + power + " is " + pow(base, power));
		}

	}

	private static class SleepingThread implements Runnable
	{
		@Override
		public void run()
		{
			//Another way to handle interrupt
			while (true)
			{
				try
				{
					Thread.sleep(1000000);
				}
				catch (InterruptedException e)
				{
					System.out.println("Interrupted");
					return;
				}
			}
		}
	}
}
