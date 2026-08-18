abstract class Employee 
{
    String name;
    int id;
    Employee(String name, int id) 
    {
        this.name = name;
        this.id = id;
    }
    abstract double monthlySalary();
    void displayInfo() 
    {
        System.out.print("ID: " + id + ", Name: " + name);
    }
}

class FullTime extends Employee 
{
    double salary;
    FullTime(String name, int id, double salary) 
    {
        super(name, id);
        this.salary = salary;
    }
    double monthlySalary() 
    {
        return salary;
    }
}

class PartTime extends Employee 
{
    int hours;
    double rate;
    PartTime(String name, int id, int hours, double rate) 
    {
        super(name, id);
        this.hours = hours;
        this.rate = rate;
    }
    double monthlySalary() 
    {
        return hours * rate;
    }
}

class Intern extends Employee 
{
    double stipend;
    Intern(String name, int id, double stipend) 
    {
        super(name, id);
        this.stipend = stipend;
    }
    double monthlySalary() 
    {
        return stipend;
    }
}

public class Payroll 
{
    public static void main(String[] args) 
    {
        Employee[] employees = 
        {
            new FullTime("Rahul", 101, 50000),
            new PartTime("Priya", 102, 80, 300),
            new Intern("Amit", 103, 15000),
            new FullTime("Neha", 104, 60000)
        };
        double total = 0;
        System.out.println("Payroll Details:");
        for (Employee e : employees) 
        {
            double salary = e.monthlySalary();
            e.displayInfo();
            System.out.printf(", Salary: %.2f", salary);
            if (e instanceof Intern) 
            {
                System.out.print(" (Intern - Stipend)");
            }
            System.out.println();
            total += salary;
        }
        System.out.printf("%nTotal Monthly Payroll = %.2f%n", total);
    }
}