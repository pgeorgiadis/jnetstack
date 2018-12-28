package os.devicemanager;

/**
 * The class Device represents a Devices of the computer system.
 * @author	Pavlos Georgiadis (aka JPG) pgeorgiadis@gmail.gr
 * @since	13.6.2005
 */
public abstract class Device {
	/** The device name. */
	public String name;
	
	/**
	 * Creates a new Device with the given device name.
	 * @param	name the device name.
	 */
	public Device(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the device name of this device.
	 * @return	the device name of this device.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the device name of this device.
	 * @param	name the new device name.
	 */
	public void setName(String name) {
		this.name = name;
	}
}
