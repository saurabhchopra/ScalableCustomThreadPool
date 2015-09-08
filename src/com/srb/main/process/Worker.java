package com.srb.main.process;

import com.srb.main.process.queue.MyQueue;

public class Worker implements Runnable {

	private MyQueue<Runnable> myQueue;
	private String name;
	public boolean isRunning = true;

	/**
	 * @param myQueue
	 * @param name
	 */
	public Worker(MyQueue<Runnable> myQueue, String name) {
		this.myQueue = myQueue;
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while (isRunning) {
			Runnable r = myQueue.dequeue();
			System.out.println("Thread Started. Name: " + name);
			r.run();
			System.out.println("Thread work completed. Name: " + name);
		}
	}
}
