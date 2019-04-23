package com.jsan.util.concurrent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 简化多线程执行的工具类。
 *
 */

public class ExecuteUtils {

	/**
	 * 返回有效执行的记录数。
	 * 
	 * @param threads 线程数
	 * @param queue 并发队列
	 * @param runner
	 * @return
	 */
	public static <T> int execute(int threads, final Queue<T> queue, final Runner<T> runner) {

		final AtomicBoolean stopFlag = new AtomicBoolean(false);
		final AtomicInteger atomicInteger = new AtomicInteger();

		ExecutorService executorService = Executors.newCachedThreadPool();

		for (int i = 0; i < threads; i++) {
			executorService.execute(new Runnable() {

				@Override
				public void run() {
					if (stopFlag.get()) {
						return;
					}

					while (true) {
						T t = queue.poll();
						if (t == null) {
							break;
						}

						if (runner.run(t)) {
							atomicInteger.getAndIncrement();
						} else {
							stopFlag.set(true);
							break;
						}
					}
				}

			});
		}

		executorService.shutdown();

		while (true) {
			if (executorService.isTerminated()) {
				return atomicInteger.get();
			}
		}

	}

	public static void main(String[] args) {

		Queue<Integer> queue = new ConcurrentLinkedQueue<>();

		for (int i = 0; i < 1000; i++) {
			queue.add(i);
		}

		int i = execute(10, queue, new Runner<Integer>() {

			@Override
			public boolean run(Integer integer) {

				if(integer>10){
					return false;
				}
				
				System.err.println(integer);
				return true;
			}
		});

		System.out.println(i);
	}
}
