package net.core.packet.container;

import java.lang.reflect.Constructor;

/**
 * ContainerFactory is a centralized PacketContainer creation factory. It is useful for
 * globally configurable PacketContainer type to be used. The strategy is to have special 
 * PacketContainer types that are faster for the machine on which this networking subsystem runs.
 * @author	JPG
 * @since	25.3.2006
 */
public class ContainerFactory {
	private Constructor constructor_int;
	
	protected ContainerFactory(Class container_class) {
		try {
			this.constructor_int = container_class.getConstructor(new Class[] {int.class});
		} catch (Exception e) {
			System.out.println("FATAL!!! ContainerFactoryService not working");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	protected ContainerFactory(String container_class_name) {
		try {
			Class container_class = Class.forName(container_class_name);
			this.constructor_int = container_class.getConstructor(new Class[] {int.class});
		} catch (Exception e) {
			System.out.println("FATAL!!! ContainerFactoryService not working");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	protected PacketContainer createContainer(int size) {
		try {
			return (PacketContainer) constructor_int
					.newInstance(new Object[] { new Integer(size) });
		} catch (Exception e) {
			System.out.println("FATAL!!! ContainerFactoryService not working");
			System.out.println(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	protected PacketContainer createContainer(byte[] data) {
		PacketContainer container = this.createContainer(data.length);
		container.write(0, data, 0, data.length);
		return container;
	}
}
