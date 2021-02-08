package com.fundamentalscoordination.interrupt.exception;

public class Main
{
	public static void main(String[] args)
	{
		Thread thread = new Thread(new BlockingTask());
		thread.start();
		thread.interrupt();
	}

	public static class BlockingTask implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				Thread.sleep(50000);
			}
			catch (InterruptedException e)
			{
				System.out.println("Blocking task interrupted.... Exiting");
			}
		}
	}
}
