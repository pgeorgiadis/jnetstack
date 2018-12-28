package net.core.services;

import os.threadpool.ThreadPool;

public class ThreadPoolService extends ThreadPool implements NetService {
	public ThreadPoolService(int initial_threads) {
		super(initial_threads);
	}
}
