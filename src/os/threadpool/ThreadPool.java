package os.threadpool;

public class ThreadPool {
	private WorkerThread[] threads;
	private int last;
	
	public ThreadPool(int size) {
		threads = new WorkerThread[size];
		
		for (int i=0; i<threads.length; i++) {
			threads[i] = new WorkerThread();
			threads[i].setName("WorkerThread-"+i);
		}
		
		last = 0;
	}
	
	public synchronized WorkerThread getThread() {
		last++;
		for (int i=last; i<threads.length; i++) {
			if (threads[i].isFree()) {
				last = i;
				return threads[i].engage();
			}
		}
		
		last = 0;
		for (int i=last; i<threads.length; i++) {
			if (threads[i].isFree()) {
				last = i;
				return threads[i].engage();
			}
		}
		
		//Generate new workers
		WorkerThread[] tmp = threads;
		threads = new WorkerThread[threads.length+10];
		System.arraycopy(tmp,0,threads,0,tmp.length);
		for (int i=tmp.length; i<threads.length; i++) {
			threads[i] = new WorkerThread();
		}
		System.out.println("resized thread pool to:"+threads.length+" threads");
		return threads[tmp.length];
	}
}
