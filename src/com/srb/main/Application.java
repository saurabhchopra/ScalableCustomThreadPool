package com.srb.main;

import com.srb.main.process.ThreadPoolManager;

public class Application {

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		Application application = new Application();
		application.startThreadPool();
	}

	/**
	 * @throws InterruptedException
	 */
	private void startThreadPool() throws InterruptedException {
		ThreadPoolManager poolManager = new ThreadPoolManager(5, 10);
		for (int i = 0; i < 35; i++) {
			poolManager.submitTask(new Runnable() {
				@Override
				public void run() {
					try {
						double i = Math.random();
						System.out.println("Task " + i + " started");
						Thread.sleep(2000);
						System.out.println("Task " + i + " completed");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}, String.valueOf(i));
		}

		Thread.sleep(20000);

		for (int i = 0; i < 35; i++) {
			poolManager.submitTask(new Runnable() {
				@Override
				public void run() {
					try {
						double i = Math.random();
						System.out.println("Task " + i + " started");
						Thread.sleep(2000);
						System.out.println("Task " + i + " completed");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}, String.valueOf(i));
		}
		
		//To shutdown the thread pool
		poolManager.shutdown();
	}
}
