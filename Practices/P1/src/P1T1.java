import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

public class P1T1 {
    public static void main(String[] args) {
        systemMain();
    }

    static void systemMain() {
        System.out.println("Welcome to Manage System!");

        ManageSystem manageSystem = new ManageSystem();
        Scanner input = new Scanner(System.in);

        int op = 0;
        do {
            System.out.println("""
                    \nPlease select operation:
                    1.Add Employee
                    2.Delete Employee
                    3. Print Info
                    0.Exit
                    """);
            try {
                op = input.nextInt();
                switch (op) {
                    case 1:
                        Employee employee = createEmployee(input);
                        if (Objects.isNull(employee)) {
                            System.out.println("Error!");
                            break;
                        }

                        System.out.println("Add new employee:" + employee + "Press y/N");
                        String check = input.next();
                        if (check.equals("y") || check.equals("Y")) {
                            manageSystem.addEmployee(employee);
                        } else {
                            System.out.println("Cancel addition");
                        }
                        break;
                    case 2:
                        System.out.println("input employee name you want to delete:");
                        String name = input.next();
                        manageSystem.removeEmployeeByName(name);
                        break;
                    case 3:
                        manageSystem.printEmployeesByAlphabet();
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Illegal option input!");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Exception:" + e);
                input.nextLine();
            }
        } while (op != 0);

        input.close();
        System.out.println("Exit Manage System!");
    }

    static Employee createEmployee(Scanner input) {
        try {
            System.out.println("Please give name:");
            String name = input.next();
            System.out.println("Please give age:");
            int age = input.nextInt();

            return new Employee(name, age);
        } catch (InputMismatchException e) {
            System.out.println("Exception:" + "Illegal input!");
            input.nextLine();
        }

        return null;
    }
}