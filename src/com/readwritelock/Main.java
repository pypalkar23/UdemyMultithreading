package com.readwritelock;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Main {
    public static final int MAX_PRIZE = 1000;

    public static void main(String[] args) {
        InventoryDatabase inventoryDatabase = new InventoryDatabase();
        Random random = new Random();

        //Setup Inventory
        for (int i = 1; i <= 100000; i++)
            inventoryDatabase.addItem(random.nextInt(MAX_PRIZE));

        Thread writer = new Thread(() -> {
            while (true) {
                inventoryDatabase.addItem(random.nextInt(MAX_PRIZE));
                inventoryDatabase.removeItem(random.nextInt(MAX_PRIZE));

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {

                }

            }
        });

        writer.setDaemon(true);
        writer.start();
        int noOfReaders = 7;
        List<Thread> readers = new ArrayList<>();

        for (int i = 0; i < noOfReaders; i++) {
            Thread thread = new Thread(() -> {
                int upperBoundPrice = random.nextInt(MAX_PRIZE);
                int lowerBoundPrice = (upperBoundPrice > 0) ? random.nextInt(upperBoundPrice) : 0;
                inventoryDatabase.getNumberOfItemInPriceRange(lowerBoundPrice, upperBoundPrice);
            });

            readers.add(thread);
        }

        long startTime = System.currentTimeMillis();

        for (Thread thread : readers)
            thread.start();

        long endTime = System.currentTimeMillis();

        System.out.println(String.format("Reading took %d ms", endTime - startTime));

    }

    public static class InventoryDatabase {
        private static TreeMap<Integer, Integer> inventoryMap = new TreeMap<>();
        //private static ReentrantLock lock = new ReentrantLock();
        private static ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        private Lock readLock = readWriteLock.readLock();
        private Lock writeLock = readWriteLock.writeLock();


        public int getNumberOfItemInPriceRange(int lowerbound, int upperbound) {
            //lock.lock();
            readLock.lock();
            try {
                Integer fromKey = inventoryMap.ceilingKey(lowerbound);
                Integer toKey = inventoryMap.floorKey(upperbound);
                NavigableMap<Integer, Integer> navigableMap = inventoryMap.subMap(fromKey, true, toKey, true);
                int sum = 0;
                for (int numberOfItemsOfPrice : navigableMap.values())
                    sum += numberOfItemsOfPrice;
                return sum;
            } finally {
                //lock.unlock();
                readLock.unlock();
            }
        }

        public void addItem(int price) {
            //lock.lock();
            writeLock.lock();
            try {
                Integer numberOfItemsForThatPrice = inventoryMap.get(price);
                if (numberOfItemsForThatPrice == null)
                    inventoryMap.put(price, 1);
                else
                    inventoryMap.put(price, numberOfItemsForThatPrice + 1);
            } finally {
                //lock.unlock();
                writeLock.unlock();
            }
        }

        public void removeItem(int price) {
            //lock.lock();
            writeLock.lock();
            try {
                Integer numberOfItemsForThatPrice = inventoryMap.get(price);
                if (numberOfItemsForThatPrice == null || numberOfItemsForThatPrice == 1)
                    inventoryMap.remove(price);
                else
                    inventoryMap.put(price, numberOfItemsForThatPrice - 1);
            } finally {
                //lock.unlock();
                writeLock.unlock();
            }
        }
    }
}
