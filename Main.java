import java.util.ArrayList;

interface IPayable {
    double calculateSalary();
    default String getPaymentDetails() {
        return String.format("Salary: %.2f", calculateSalary());
    }
}


class InvalidEmployeeException extends RuntimeException {
    InvalidEmployeeException(String msg) { super(msg); }
}

abstract class Employee implements IPayable {
    private final String name;
    private final int id;
    
    public Employee(String name, int id) {
        if(name == null || name.trim().isEmpty()) 
            throw new InvalidEmployeeException("Invalid name");
        if(id <= 0) 
            throw new InvalidEmployeeException("Invalid ID");
        this.name = name;
        this.id = id;
    }
    
    public String getName() { return name; }
    public int getId() { return id; }
    
    @Override
    public String toString() {
        return String.format("%s[id=%d, %s]", 
            getClass().getSimpleName(), id, getPaymentDetails());
    }
}

class FullTimeEmployee extends Employee {
    private final double monthlySalary;
    
    public FullTimeEmployee(String name, int id, double monthlySalary) {
        super(name, id);
        if(monthlySalary < 0) 
            throw new InvalidEmployeeException("Invalid salary");
        this.monthlySalary = monthlySalary;
    }
    
    @Override
    public double calculateSalary() { return monthlySalary; }
}

class PartTimeEmployee extends Employee {
    private final int hoursWorked;
    private final double hourlyRate;
    
    public PartTimeEmployee(String name, int id, int hoursWorked, double hourlyRate) {
        super(name, id);
        if(hoursWorked < 0 || hourlyRate < 0) 
            throw new InvalidEmployeeException("Invalid hours/rate");
        this.hoursWorked = hoursWorked;
        this.hourlyRate = hourlyRate;
    }
    
    @Override
    public double calculateSalary() { return hoursWorked * hourlyRate; }
}

class PayrollSystem {
    private final ArrayList<Employee> employees = new ArrayList<>();
    
    public void addEmployee(Employee emp) {
        if(employees.stream().anyMatch(e -> e.getId() == emp.getId()))
            throw new InvalidEmployeeException("Duplicate ID");
        employees.add(emp);
    }
    
    public void removeEmployee(int id) {
        if(!employees.removeIf(e -> e.getId() == id))
            throw new InvalidEmployeeException("Employee not found");
    }
    
    public void displayEmployees() {
        employees.forEach(System.out::println);
    }
}

public class Main {
    public static void main(String[] args) {
        PayrollSystem payroll = new PayrollSystem();
        
        try {
            payroll.addEmployee(new FullTimeEmployee("Hari", 1, 5000));
            payroll.addEmployee(new PartTimeEmployee("Giri", 2, 20, 15));
            
            System.out.println("All Employees:");
            payroll.displayEmployees();
            
            payroll.removeEmployee(2);
            System.out.println("\nAfter Removal:");
            payroll.displayEmployees();
            
        } catch (InvalidEmployeeException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}