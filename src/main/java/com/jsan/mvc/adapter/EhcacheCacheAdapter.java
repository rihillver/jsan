package com.jsan.mvc.adapter;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import com.jsan.mvc.View;

/**
 * Ehcache 适配器。
 *
 */

public class EhcacheCacheAdapter extends AbstractCacheAdapter {

	@Override
	public void setCache(String cacheName, String cacheKey, View view) {

		put(cacheName, cacheKey, view);
	}

	@Override
	public View getCache(String cacheName, String cacheKey) {

		return (View) get(cacheName, cacheKey);
	}

	// ==================================================

	private CacheManager cacheManager = CacheManager.create(); // 返回的是单例

	/**
	 * 返回 CacheManager 。
	 * 
	 * @return
	 */
	@SuppressWarnings("unused")
	private CacheManager getCacheManager() {

		return cacheManager;
	}

	/**
	 * 返回指定缓存名的 Cache 对象。
	 * 
	 * @param cacheName
	 * @return
	 */
	private Cache getCache(String cacheName) {

		return cacheManager.getCache(cacheName);
	}

	/**
	 * 增加（或修改）指定缓存名和键名的缓存对象。
	 * 
	 * @param cacheName
	 * @param key
	 * @param value
	 */
	private void put(String cacheName, Object key, Object value) {

		Element element = new Element(key, value);
		getCache(cacheName).put(element);
	}

	/**
	 * 返回指定缓存名和键名的对象。
	 * 
	 * @param cacheName
	 * @param key
	 * @return
	 */
	private Object get(String cacheName, Object key) {

		Element element = getCache(cacheName).get(key);
		return element == null ? null : element.getObjectValue();
	}

	/**
	 * 增加（或修改）指定缓存名的 Element 对象。
	 * 
	 * @param cacheName
	 * @param element
	 */
	@SuppressWarnings("unused")
	private void putElement(String cacheName, Element element) {

		getCache(cacheName).put(element);
	}

	/**
	 * 返回指定缓存名和键名的 Element 对象。
	 * 
	 * @param cacheName
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unused")
	private Element getElement(String cacheName, Object key) {

		return getCache(cacheName).get(key);
	}

}
