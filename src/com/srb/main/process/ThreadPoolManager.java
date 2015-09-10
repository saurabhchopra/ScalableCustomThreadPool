package com.srb.main.process;

import java.util.LinkedList;
import java.util.Queue;

import com.srb.main.process.queue.MyQueue;

public class ThreadPoolManager {
	private final int MIN_THREAD_POOL_CAPACITY;
	private final int MAX_THREAD_POOL_CAPACITY;
	private MyQueue<Runnable> myQueue = new MyQueue<Runnable>();
	private Queue<Worker> workerList = new LinkedList<Worker>();
	private boolean isRunning = true;

	/**
	 * @param minCapacity
	 * @param maxCapacity
	 */
	public ThreadPoolManager(int minCapacity, int maxCapacity) {
		MIN_THREAD_POOL_CAPACITY = minCapacity;
		MAX_THREAD_POOL_CAPACITY = maxCapacity;
		initAllConsumers();
	}

	/**
	 * 
	 */
	private void initAllConsumers() {
		for (int i = 1; i <= MIN_THREAD_POOL_CAPACITY; i++) {
			Worker worker = new Worker(myQueue, "Thread" + i);
			Thread t = new Thread(worker);
			t.start();
			workerList.add(worker);
		}
		watcher();
	}

	/**
	 * This method is used add watch on thread pool and scale the size of thread
	 * pool accordingly
	 */
	private void watcher() {
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
				while (isRunning) {
					if ((myQueue.getQueueLength()) > MIN_THREAD_POOL_CAPACITY
							&& workerList.size() < MAX_THREAD_POOL_CAPACITY) {
						for (int i = MIN_THREAD_POOL_CAPACITY + 1; i <= MAX_THREAD_POOL_CAPACITY; i++) {
							Worker worker = new Worker(myQueue, "Thread" + i);
							Thread t = new Thread(worker);
							t.start();
							workerList.add(worker);
						}
					} else if ((myQueue.getQueueLength()) < MIN_THREAD_POOL_CAPACITY
							&& workerList.size() > MIN_THREAD_POOL_CAPACITY) {
						for (int i = MIN_THREAD_POOL_CAPACITY + 1; i <= workerList.size(); i++) {
							Worker worker = workerList.remove();
							worker.isRunning = false;
						}
					}
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("workerList size" + workerList.size());
				}
			}
		});
		th.start();
	}

	/**
	 * This method is used to shutdown the thread pool
	 */
	public void shutdown() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				boolean iscurrentThreadRunning = true;
				while (iscurrentThreadRunning) {
					System.out.println("Task queue size = " + myQueue.getQueueLength());
					if (myQueue.getQueueLength() == 0) {
						for (Worker worker : workerList) {
							worker.isRunning = false;
						}
						myQueue.activateAllThread();
						workerList.clear();
						// Shutdown watcher
						isRunning = false;
						System.out.println("Thread pool shutdown");
						iscurrentThreadRunning = false;
					}
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});

		thread.start();
	}

	/**
	 * @param r
	 * @param threadName
	 */
	public void submitTask(Runnable r, String threadName) {
		myQueue.enqueue(r);
	}
}
