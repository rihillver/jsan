package com.jsan.dao.map;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class HashSetMultiValueMap<K, V> implements SetMultiValueMap<K, V>, Serializable {

	private static final long serialVersionUID = 1L;

	private final Map<K, Set<V>> targetMap;

	public HashSetMultiValueMap() {
		this.targetMap = new LinkedHashMap<K, Set<V>>();
	}

	public HashSetMultiValueMap(int initialCapacity) {
		this.targetMap = new LinkedHashMap<K, Set<V>>(initialCapacity);
	}

	public HashSetMultiValueMap(Map<K, Set<V>> otherMap) {
		this.targetMap = new LinkedHashMap<K, Set<V>>(otherMap);
	}

	// MultiValueMap implementation

	@Override
	public void add(K key, V value) {
		Set<V> values = this.targetMap.get(key);
		if (values == null) {
			values = new HashSet<V>();
			this.targetMap.put(key, values);
		}
		values.add(value);
	}

	@Override
	public V getFirst(K key) {
		Set<V> values = this.targetMap.get(key);
		return getSetFirst(values);
	}

	private V getSetFirst(Set<V> set) {
		if (set != null) {
			Iterator<V> iterator = set.iterator();
			return iterator.next();
		} else {
			return null;
		}
	}

	private V getSetLast(Set<V> set) {
		if (set != null) {
			V v = null;
			Iterator<V> iterator = set.iterator();
			while (iterator.hasNext()) {
				v = iterator.next();
			}
			return v;
		} else {
			return null;
		}
	}

	@Override
	public V getLast(K key) {
		Set<V> values = this.targetMap.get(key);
		return getSetLast(values);
	}

	@Override
	public void set(K key, V value) {
		Set<V> values = new HashSet<V>();
		values.add(value);
		this.targetMap.put(key, values);
	}

	@Override
	public void setAll(Map<K, V> values) {
		for (Entry<K, V> entry : values.entrySet()) {
			set(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public Map<K, V> toSingleValueMap() {
		LinkedHashMap<K, V> singleValueMap = new LinkedHashMap<K, V>(this.targetMap.size());
		for (Entry<K, Set<V>> entry : this.targetMap.entrySet()) {
			singleValueMap.put(entry.getKey(), getSetFirst(entry.getValue()));
		}
		return singleValueMap;
	}

	// Map implementation

	@Override
	public int size() {
		return this.targetMap.size();
	}

	@Override
	public boolean isEmpty() {
		return this.targetMap.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		return this.targetMap.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return this.targetMap.containsValue(value);
	}

	@Override
	public Set<V> get(Object key) {
		return this.targetMap.get(key);
	}

	@Override
	public Set<V> put(K key, Set<V> value) {
		return this.targetMap.put(key, value);
	}

	@Override
	public Set<V> remove(Object key) {
		return this.targetMap.remove(key);
	}

	@Override
	public void putAll(Map<? extends K, ? extends Set<V>> map) {
		this.targetMap.putAll(map);
	}

	@Override
	public void clear() {
		this.targetMap.clear();
	}

	@Override
	public Set<K> keySet() {
		return this.targetMap.keySet();
	}

	@Override
	public Collection<Set<V>> values() {
		return this.targetMap.values();
	}

	@Override
	public Set<Entry<K, Set<V>>> entrySet() {
		return this.targetMap.entrySet();
	}

	@Override
	public HashSetMultiValueMap<K, V> clone() {
		return new HashSetMultiValueMap<K, V>(this);
	}

	public HashSetMultiValueMap<K, V> deepCopy() {
		HashSetMultiValueMap<K, V> copy = new HashSetMultiValueMap<K, V>(this.targetMap.size());
		for (Map.Entry<K, Set<V>> entry : this.targetMap.entrySet()) {
			copy.put(entry.getKey(), new HashSet<V>(entry.getValue()));
		}
		return copy;
	}

	@Override
	public boolean equals(Object obj) {
		return this.targetMap.equals(obj);
	}

	@Override
	public int hashCode() {
		return this.targetMap.hashCode();
	}

	@Override
	public String toString() {
		return this.targetMap.toString();
	}

}
