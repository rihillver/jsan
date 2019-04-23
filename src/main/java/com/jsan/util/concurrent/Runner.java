package com.jsan.util.concurrent;

/**
 * 执行接口。
 *
 * @param <T>
 */

public interface Runner<T> {

	/**
	 * 返回 true 则继续有效的执行后面的线程，返回 false 则不再有效的执行后面的线程。
	 * 
	 * @param t
	 * @return
	 */
	public boolean run(T t);
}
