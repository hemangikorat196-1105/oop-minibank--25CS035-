import java.util.Objects;

public class Point {
    private final int x;
    private final int y;

    // Constructor
    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // toString()
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    // equals()
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Point))
            return false;

        Point p = (Point) obj;
        return x == p.x && y == p.y;
    }

    // hashCode()
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}