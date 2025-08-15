package co.in.nnj.learn.fun;

public class Employee {
    private final String name;
    private final double salary;

    public Employee(final String name, final double salary) {
        this.name = name;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }
    public double getSalary() {
        return salary;
    }
}
