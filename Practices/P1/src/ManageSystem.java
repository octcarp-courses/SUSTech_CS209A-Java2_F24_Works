import java.util.ArrayList;
import java.util.Comparator;

public class ManageSystem {
    private ArrayList<Employee> employees;

    public ManageSystem() {
        employees = new ArrayList<>();
    }

    public void addEmployee(Employee newEmployee) {
        String name = newEmployee.getName();

        for (Employee employee : employees) {
            if (employee.getName().equals(name)) {
                System.out.println("Cannot add employee with the same name!");
                return;
            }
        }

        employees.add(newEmployee);
        System.out.println("Successfully added! Info:" + newEmployee);
    }

    public void removeEmployeeByName(String name) {
        for (Employee employee : employees) {
            if (employee.getName().equals(name)) {
                employees.remove(employee);
                System.out.println("Successfully deleted! Info:" + employee);
                return;
            }
        }

        System.out.println("Cannot find name: " + name);
    }

    public void printEmployeesByAlphabet() {
        ArrayList<Employee> orderedEmployees = new ArrayList<>(employees);
        orderedEmployees.sort(Comparator.comparing(Employee::getName));

        for (Employee employee : orderedEmployees) {
            System.out.println(employee);
        }
    }
}
