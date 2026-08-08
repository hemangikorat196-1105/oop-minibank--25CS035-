import java.util.Scanner;

record Vehicle(String number, String type) {}

public class TollBooth {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int totalToll = 0;
        int bikeCount = 0;
        int carCount = 0;
        int truckCount = 0;

        while (true) {
            System.out.print("Enter vehicle number (or done to stop): ");
            String number = sc.next();

            if (number.equalsIgnoreCase("done")) {
                break;
            }

            System.out.print("Enter vehicle type (bike/car/truck): ");
            String type = sc.next().toLowerCase();

            Vehicle vehicle = new Vehicle(number, type);

            try {
                int toll = switch (vehicle.type()) {
                    case "bike" -> {
                        bikeCount++;
                        yield 20;
                    }
                    case "car" -> {
                        carCount++;
                        yield 50;
                    }
                    case "truck" -> {
                        truckCount++;
                        yield 150;
                    }
                    default -> throw new IllegalArgumentException("Invalid vehicle type");
                };

                totalToll += toll;
                System.out.println("Toll Charged: " + toll);

            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("\nTotal Toll Collected: " + totalToll);

        if (bikeCount >= carCount && bikeCount >= truckCount) {
            System.out.println("Highest Count: Bike (" + bikeCount + ")");
        } else if (carCount >= bikeCount && carCount >= truckCount) {
            System.out.println("Highest Count: Car (" + carCount + ")");
        } else {
            System.out.println("Highest Count: Truck (" + truckCount + ")");
        }

        sc.close();
    }
}