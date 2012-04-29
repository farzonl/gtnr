package com.gtnightrover.dfrduino;

public class ServerManager extends Thread {

	private Lidar lidar;
	private Atom atom;
	private BatteryController batteryController;
	private MotorController motorController;
	
	public ServerManager(Lidar lidar, Atom atom, BatteryController bc, MotorController mc) {
		super();
		if (lidar == null || atom == null || bc == null || mc == null)
			throw new IllegalArgumentException();
		this.lidar = lidar;
		this.atom = atom;
		this.batteryController = bc;
		this.motorController = mc;
	}
	
	@Override
	public void run() {
		while(true) {
			lidar.getDistances();
			atom.update();
			batteryController.update();
			motorController.update();
			try {
			Thread.sleep(1000);
			} catch (InterruptedException e){ }
		}
	}
}
