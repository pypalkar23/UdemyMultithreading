package com.threadInheritance;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main
{

	public static final int MAX_PASSWORD = 9999;

	public static void main(String[] args)
	{

		Random random = new Random();
		Vault vault = new Vault(random.nextInt(MAX_PASSWORD));

		List<Thread> threads = new ArrayList<>();
		Thread ascendingThread = new AscendingHackerThread(vault);
		Thread descendingThread = new DescendingHackerThread(vault);
		Thread policeThread = new PoliceThread();

		threads.add(ascendingThread);
		threads.add(descendingThread);
		threads.add(policeThread);

		for (Thread thread : threads)
		{
			thread.start();
		}
	}

	private static class Vault
	{

		private int password;

		public Vault(int password)
		{
			this.password = password;
		}

		public boolean isCorrectPassword(int guess)
		{
			try
			{
				Thread.sleep(5);
			}
			catch (InterruptedException e)
			{
				System.out.println("Thread interrupted ");
				e.printStackTrace();
			}
			return this.password == guess;
		}
	}

	private static abstract class HackerThread extends Thread
	{
		protected Vault vault;

		public HackerThread(Vault vault)
		{
			this.vault = vault;
			this.setName(this.getClass().getSimpleName());
			this.setPriority(Thread.MAX_PRIORITY);
		}

		@Override
		public void start()
		{
			System.out.println("Starting thread" + this.getName());
			super.start();
		}
	}

	private static class DescendingHackerThread extends HackerThread
	{
		public DescendingHackerThread(Vault vault)
		{
			super(vault);
		}

		@Override
		public void run()
		{
			for (int guess = MAX_PASSWORD; guess >= 0; guess--)
			{
				if (vault.isCorrectPassword(guess))
				{
					System.out.println("Guessed the password by " + this.getName());
					System.exit(0);
				}
			}
		}
	}

	private static class AscendingHackerThread extends HackerThread
	{
		public AscendingHackerThread(Vault vault)
		{
			super(vault);
		}

		@Override
		public void run()
		{
			for (int guess = 0; guess < MAX_PASSWORD; guess++)
			{
				if (vault.isCorrectPassword(guess))
				{
					System.out.println("Guessed the password by " + this.getName());
					System.exit(0);
				}
			}
		}
	}

	private static class PoliceThread extends Thread
	{
		@Override
		public void run()
		{
			for (int i = 0; i < 10; i++)
			{
				try
				{
					Thread.sleep(2000);
				}
				catch (InterruptedException e)
				{
					System.out.println("Thread Interrupted");
				}
				System.out.println(i);
			}
			System.out.println("Game over for you");
			System.exit(0);
		}
	}
}


