package com.jagex.link;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.

// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 



public final class Cache {

	public Cache(int i) {
		empty = new Cacheable();
		history = new Queue();
		initialCount = i;
		unused = i;
		table = new HashTable();
	}

	public Cacheable get(long l) {
		Cacheable cacheable = (Cacheable) table.get(l);
		if (cacheable != null) {
			history.push(cacheable);
		}
		return cacheable;
	}

	public void put(long l, Cacheable cacheable) {
		if (unused == 0) {
			Cacheable nodeSub_1 = history.pop();
			nodeSub_1.unlink();
			nodeSub_1.unlinkCacheable();
			if (nodeSub_1 == empty) {
				Cacheable nodeSub_2 = history.pop();
				nodeSub_2.unlink();
				nodeSub_2.unlinkCacheable();
			}
		} else {
			unused--;
		}
		table.put(l, cacheable);
		history.push(cacheable);
	}

	public void clear() {
		do {
			Cacheable cacheable = history.pop();
			if (cacheable != null) {
				cacheable.unlink();
				cacheable.unlinkCacheable();
			} else {
				unused = initialCount;
				return;
			}
		} while (true);
	}

	private final Cacheable empty;
	private final int initialCount;
	private int unused;
	private final HashTable table;
	private final Queue history;
}
