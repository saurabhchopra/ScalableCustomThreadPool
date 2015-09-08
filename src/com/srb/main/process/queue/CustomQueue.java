package com.srb.main.process.queue;

public interface CustomQueue<E> {
	public void enqueue(E e);

	public E dequeue();
}
