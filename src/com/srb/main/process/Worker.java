package com.srb.main.process;

import com.srb.main.process.queue.MyQueue;

public class Worker implements Runnable {

	private MyQueue<Task> myQueue;
	private String name;
	public boolean isRunning = true;

	/**
	 * @param myQueue
	 * @param name
	 */
	public Worker(MyQueue<Task> myQueue, String name) {
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
			Task t = myQueue.dequeue();
			if (t != null) {
				System.out.println("Thread Started. Name: " + name);
				t.action();
				System.out.println("Thread work completed. Name: " + name);
			}
		}
	}
}
