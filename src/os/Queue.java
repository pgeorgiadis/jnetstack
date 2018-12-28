package os;

/**
 * The class Queue is a utility class that implements a clasic queue (FIFO). It uses a Object[]
 * as a ring buffer to store the queues objects.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	19.8.2005
 */
public class Queue {
	/** The object array that holds the queue objects. */
	private Object[] queue;
	/** The r_head holds the current position of the read head. */
	private int r_head;
	/** The w_head holds the current position of the write head. */
	private int w_head;
	
	/**
	 * Creates a new queue.
	 */
	public Queue() {
		this.queue = new Object[30];
	}
	
	/**
	 * Gets the next element of this queue.
	 * @return the next element of this queue.
	 */
	public Object get() {
		if (r_head == w_head) return null;
		Object o = queue[r_head];
		queue[r_head++] = null;
		if (r_head == queue.length) r_head = 0;
		return o;
	}
	
	/**
	 * Puts an object to the queue.
	 * @param o the object that will be added to the queue.
	 */
	public void put(Object o) {
		queue[w_head++] = o;
		if (w_head == queue.length) w_head = 0;
		if (w_head == r_head) {
			Object[] tmp = new Object[queue.length+10];
			System.arraycopy(queue, r_head, tmp, 0, queue.length-r_head);
			System.arraycopy(queue, 0, tmp, queue.length-r_head, r_head);
			queue = tmp;
		}
	}
	
	/**
	 * Peeks at the first object of the queue without to remove it.
	 * @return the first object at the queue.
	 */
	public Object peek() {
		return queue[r_head];
	}
	
	/**
	 * The current size of this queue. The number of the objects that are currently in this queue.
	 * @return the current size of this queue.
	 */
	public int getSize(){
		if (r_head > w_head)
			return queue.length - r_head + w_head;
		else
			return w_head - r_head;
	}
}
