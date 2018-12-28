package net.core.packet.container;


public class MeasureContainers {
	public static void main(String[] args) {
		PacketContainer[] containers = new PacketContainer[] {
				new ByteArrayContainer(1),
				new ByteBufferContainer(1),
				new DirectByteBufferContainer(1)
				};
		for (int i=0; i<containers.length; i++) {
			float speed = MeasureContainers.measureSingleByteRW(containers[i]);
			String msg = containers[i].getClass().getName()+" single byte rw speed is "+speed+" Mbytes/s";
			System.out.println(msg);
		}
	}
	
	public static float measureSingleByteRW(PacketContainer s) {
		PacketContainer d = s.getCopy();
		long start = 0, stop = 0;
		start = System.currentTimeMillis();
		for (int i=0; i<1048576; i++) {
			d.u_write(0, s.u_read(0));
		}
		stop = System.currentTimeMillis();
		long duration = stop - start;
		float speed = 1048576 / duration;
		speed = speed * 1000 / 1024 / 1024;
		return speed;
	}
}