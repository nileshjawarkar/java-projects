package co.in.nnj.learn.fun;

public class Employee implements Comparable<Employee> {
    private final Long id;
    private final String name;
    private final double salary;
    private final String designation;
    private final String city;
    private final String gender;

    private Employee(final EmployeeBuilder builder) {
        this.name = builder.name;
        this.salary = builder.salary;
        this.designation = builder.designation;
        this.city = builder.city;
        this.gender = builder.gender;
        this.id = builder.id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }

    public String getDesignation() {
        return designation;
    }

    public String getCity() {
        return city;
    }

    public String getGender() {
        return gender;
    }

    @Override
    public String toString() {
        return "Employee{id=" + id + ", name=" + name + ", salary=" + salary + ", designation=" + designation
                + ", city=" + city + ", gender=" + gender + "}";
    }

    public static class EmployeeBuilder {
        private String name;
        private double salary;
        private String designation;
        private String city;
        private String gender;
        private Long id;

        public EmployeeBuilder withId(final Long id) {
            this.id = id;
            return this;
        }

        public EmployeeBuilder withName(final String name) {
            this.name = name;
            return this;
        }

        public EmployeeBuilder withSalary(final double salary) {
            this.salary = salary;
            return this;
        }

        public EmployeeBuilder withDesignation(final String designation) {
            this.designation = designation;
            return this;
        }

        public EmployeeBuilder withCity(final String city) {
            this.city = city;
            return this;
        }

        public EmployeeBuilder withGender(final String gender) {
            this.gender = gender;
            return this;
        }

        public Employee build() {
            return new Employee(this);
        }
    }

    public static EmployeeBuilder builder() {
        return new EmployeeBuilder();
    }

    @Override
    public int compareTo(final Employee o) {
        if (salary < o.salary) {
            return -1;
        } else if (salary > o.salary) {
            return 1;
        } else {
            return 0;
        }
    }
}
