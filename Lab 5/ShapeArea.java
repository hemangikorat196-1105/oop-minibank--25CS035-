abstract class Shape 
{
    abstract double area();
}

class Circle extends Shape 
{
    double radius;
    Circle(double radius) 
    {
        this.radius = radius;
    }
    double area() 
    {
        return Math.PI * radius * radius;
    }
}

class Rectangle extends Shape 
{
    double length, width;
    Rectangle(double length, double width) 
    {
        this.length = length;
        this.width = width;
    }
    double area() 
    {
        return length * width;
    }
}

class Triangle extends Shape 
{
    double base, height;
    Triangle(double base, double height) {
        this.base = base;
        this.height = height;
    }
    double area() 
    {
        return 0.5 * base * height;
    }
}

public class ShapeArea
{
    public static void main(String[] args) {

        Shape[] shapes = 
        {
            new Circle(5),
            new Rectangle(10, 4),
            new Triangle(8, 6)
        };
        double total = 0;
        double largest = 0;
        System.out.println("Shape Areas:");
        for (Shape s : shapes) 
        {
            double a = s.area();
            System.out.printf("%s Area = %.2f%n",s.getClass().getSimpleName(), a);
            total += a;
            if (a > largest) 
            {
                largest = a;
            }
        }
        System.out.printf("%nTotal Area = %.2f%n", total);
        System.out.printf("Largest Area = %.2f%n", largest);
    }
}