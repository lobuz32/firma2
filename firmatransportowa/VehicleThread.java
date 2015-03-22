package firmatransportowa;

import java.util.List;

public class VehicleThread extends Thread {

	private TransportCompany transportCompany;
	private Vehicle vehicle;
	private TransportTask currentTransportTask;

	public VehicleThread(TransportCompany transportCompany, Vehicle vehicle) {

		this.transportCompany = transportCompany;
		this.vehicle = vehicle;
		System.out.println("Vehicle " + vehicle.getId() + " started!"
				+ " (Fuel: " + vehicle.getFuel() + "L)");
	}

	public void sleep() {
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sleepBetweenStations() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			if (currentTransportTask == null) {
				TransportTask task = transportCompany.lookForTask(vehicle);
				if (task == null) {
					System.out.println("Vehicle id: " + this.vehicle.getId()
							+ " has not found a Task.");
					sleep();

				} else {
					System.out.println("Vehicle id: " + this.vehicle.getId()
							+ " has found a Task.");
					currentTransportTask = task;
					sleepBetweenStations();
					runTransport();
					sleep();
				}
			}
		}
	}

	public void runTransport() {
		int currentStation = 0;
		List<GasStation> gasStationList = currentTransportTask
				.getGasStationList();
		int distanceBetweenStations = gasStationList.get(0)
				.getMeanDistanceToEnd();

		while (true) {
			if (currentStation < gasStationList.size()) {
				int currentStationForText = currentStation+1;
				System.out.println("Vehicle id : " + vehicle.getId()
						+ " is at station id " + currentStationForText + " ("
						+ currentStationForText + "/" + gasStationList.size()
						+ ")");

				if (vehicle.getFuel() > distanceBetweenStations) {
					System.out.println("\tSkip current station.");

				} else {
					vehicle.setMaxFuel();
					System.out.println("\tRefuel in current station.");
				}

				// System.out.println("DIS:" + distanceBetweenStations);
				vehicle.setFuel(vehicle.getFuel() - distanceBetweenStations);

				// System.out.println("FUEL OUT: " + vehicle.getFuel());

				currentStation++;

				sleepBetweenStations();

			} else {
				currentTransportTask = null;
				System.out.println("Vehicle id : " + vehicle.getId()
						+ " finished task. Vehicle is now looking for tasks.");
				break;
			}

		}

	}

}
