package com.srb.main.process;

import java.util.LinkedList;
import java.util.Queue;

import com.srb.main.process.queue.MyQueue;

public class ThreadPoolManager {
	private final int THREAD_POOL_CAPACITY;
	private final int MAX_THREAD_POOL_CAPACITY = 12;
	private MyQueue<Runnable> myQueue = new MyQueue<Runnable>();
	private Queue<Worker> workerList = new LinkedList<Worker>();

	/**
	 * @param capacity
	 */
	public ThreadPoolManager(int capacity) {
		THREAD_POOL_CAPACITY = capacity;
		initAllConsumers();
	}

	/**
	 * 
	 */
	private void initAllConsumers() {
		for (int i = 1; i <= THREAD_POOL_CAPACITY; i++) {
			Worker worker = new Worker(myQueue, "Thread" + i);
			Thread t = new Thread(worker);
			t.start();
			workerList.add(worker);
		}
		watcher();
	}

	/**
	 * 
	 */
	private void watcher() {
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if ((myQueue.getQueueLength()) > THREAD_POOL_CAPACITY
							&& workerList.size() < MAX_THREAD_POOL_CAPACITY) {

						for (int i = THREAD_POOL_CAPACITY + 1; i <= MAX_THREAD_POOL_CAPACITY; i++) {
							Worker worker = new Worker(myQueue, "Thread" + i);
							Thread t = new Thread(worker);
							t.start();
							workerList.add(worker);
						}

					} else if ((myQueue.getQueueLength()) < THREAD_POOL_CAPACITY
							&& workerList.size() > THREAD_POOL_CAPACITY) {
						for (int i = THREAD_POOL_CAPACITY + 1; i <= workerList.size(); i++) {
							Worker worker = workerList.remove();
							worker.isRunning = false;
						}
					}
					System.out.println("workerList size" + workerList.size());
				}
			}
		});
		th.start();
	}

	/**
	 * @param r
	 * @param threadName
	 */
	public void submitTask(Runnable r, String threadName) {
		myQueue.enqueue(r);
	}
}
