package com.fundamentalscoordination.join;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main
{
	public static void main(String[] args) throws InterruptedException
	{
		List<Long> inputNumbers = Arrays.asList(1000000L, 3435L, 35433L, 4656L, 23L, 2435L, 5566L);

		List<FactorialThread> threads = new ArrayList<>();

		for (long inputNumber : inputNumbers)
		{
			threads.add(new FactorialThread(inputNumber));
		}

		for (Thread thread : threads)
		{
			thread.start();
		}
		for (Thread thread : threads)
		{
			//miliseconds till main thread will wait for the other one
			thread.join(1000);
		}

		for (int i = 0; i < inputNumbers.size(); i++)
		{
			FactorialThread factorialThread = threads.get(i);
			if (factorialThread.isFinished())
			{
				System.out.println("Factorial of " + inputNumbers.get(i) + " is " + factorialThread.getResult());
			}
			else
			{
				System.out.println(String.format("Factorial of %d is in still progress hence interrupting result:%d", inputNumbers.get(i), factorialThread.getResult()));
				factorialThread.interrupt();

			}

		}
	}

	public static class FactorialThread extends Thread
	{
		private long inputNumber;

		private BigInteger result = BigInteger.ZERO;

		private boolean isFinished = false;

		public FactorialThread(long inputNumber)
		{
			this.inputNumber = inputNumber;
		}

		@Override
		public void run()
		{
			this.result = factorial(inputNumber);
			this.isFinished = true;
		}

		public BigInteger factorial(long n)
		{
			BigInteger tempResult = BigInteger.ONE;

			for (long i = n; i > 0; i--)
			{
				if (this.isInterrupted())
				{
					System.out.println(String.format("Thread %s is interrupted", this.getName()));
					return BigInteger.ONE;
				}
				tempResult = tempResult.multiply(new BigInteger(Long.toString(i)));
			}

			return tempResult;
		}

		public boolean isFinished()
		{
			return isFinished;
		}

		public BigInteger getResult()
		{
			return result;
		}

	}

}
