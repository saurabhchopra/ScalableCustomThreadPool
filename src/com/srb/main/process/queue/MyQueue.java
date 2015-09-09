package com.srb.main.process.queue;

import java.util.LinkedList;
import java.util.Queue;

public class MyQueue<E> implements CustomQueue<E> {

	private Queue<E> queue = new LinkedList<E>();

	/**
	 * @return
	 */
	public int getQueueLength() {
		return queue.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.srb.main.queue.CustomQueue#enqueue(java.lang.Object)
	 */
	@Override
	public synchronized void enqueue(E e) {
		queue.add(e);
		notifyAll();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.srb.main.queue.CustomQueue#dequeue()
	 */
	@Override
	public synchronized E dequeue() {
		E e = null;
		try {
			if (queue.isEmpty()) {
				try {
					wait();
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
			e = queue.remove();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return e;
	}
}
