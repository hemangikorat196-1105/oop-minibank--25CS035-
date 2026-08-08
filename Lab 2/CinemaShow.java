class Show {
    private String title;
    private int seatsAvailable;
    private final int capacity;
    private static int totalBooked = 0;

    Show(String title, int capacity) {
        this.title = title;
        this.capacity = capacity;
        this.seatsAvailable = capacity;
    }

    Show(String title) {
        this(title, 100);
    }

    boolean book(int n) {
        if (n <= seatsAvailable) {
            seatsAvailable -= n;
            totalBooked += n;
            return true;
        } else {
            return false;
        }
    }

    void cancel(int n) {
        seatsAvailable += n;
        if (seatsAvailable > capacity) {
            seatsAvailable = capacity;
        }
    }

    int getSeatsAvailable() {
        return seatsAvailable;
    }

    public static int getTotalBooked() {
        return totalBooked;
    }
}

public class CinemaShow {
    public static void main(String[] args) {

        Show show = new Show("Avengers", 50);

        System.out.println("Book 20: " + show.book(20));
        System.out.println("Seats Available: " + show.getSeatsAvailable());

        System.out.println("Book 15: " + show.book(15));
        System.out.println("Seats Available: " + show.getSeatsAvailable());

        System.out.println("Book 30: " + show.book(30));
        System.out.println("Seats Available: " + show.getSeatsAvailable());

        show.cancel(10);
        System.out.println("After Cancel, Seats Available: " + show.getSeatsAvailable());

        System.out.println("Book 25: " + show.book(25));
        System.out.println("Seats Available: " + show.getSeatsAvailable());

        System.out.println("Total Booked: " + Show.getTotalBooked());
    }
}