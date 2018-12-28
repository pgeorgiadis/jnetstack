package net.core;

/**
 * AbstractNetModule implements a basic functionality that is very common among the 
 * NetModules. Most of the modules extend this abstract class.
 * @author	Pavlos Georgiadis (aka JPG) jpg@freemail.gr
 * @since	4.4.2005
 */
public abstract class AbstractNetModule implements NetModule {
	protected NetCore core;
	
	public AbstractNetModule(NetCore core) {
		this.core = core;
	}
	
	public NetCore core() {
		return core;
	}
	
	public void log(int level, String message) {
		core.getLoggingService().log(level, message);
	}
	
	public void log(int level, Throwable t) {
		core.getLoggingService().log(level, t);
	}

	public void initialize(Object config) {}

	public void shutdown() {}
}
