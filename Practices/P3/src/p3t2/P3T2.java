package p3t2;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class P3T2 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        int op = 0;
        do {
            System.out.print("""
                Please input the function no:
                1 - Get even numbers
                2 - Get odd numbers
                3 - Get prime numbers
                0 - Quit
                """);
            try {
                try {
                    op = Integer.parseInt(input.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Illegal Input!");
                }

                if (op == 0) {
                    return;
                }

                MyPredicate<Integer> myPredicate = switch (op) {
                    case 1 -> FilterUtil.isEven;
                    case 2 -> FilterUtil.isOdd;
                    case 3 -> FilterUtil.isPrime;
                    default -> null;
                };

                if (myPredicate == null) {
                    System.out.println("Illegal function");
                    continue;
                }

                System.out.println("Input the integer list:");
                String inputLine = input.nextLine();

                String[] parts = inputLine.split("\\s+");
                List<Integer> numbers = new ArrayList<>();
                for (String part : parts) {
                    try {
                        Integer number = Integer.parseInt(part);
                        numbers.add(number);
                    } catch (NumberFormatException e) {
                        System.out.println("Illegal input part: " + part);
                    }
                }

                List<Integer> filtered = FilterUtil.filter(numbers, myPredicate);
                System.out.println(filtered);
            } catch (InputMismatchException e) {
                System.out.println("Exception:" + e);
                input.nextLine();
            }
        } while (op != 0);

        input.close();
    }
}

class FilterUtil {
    static MyPredicate<Integer> isEven = x -> x % 2 == 0;

    static MyPredicate<Integer> isOdd = x -> x % 2 != 0;

    static MyPredicate<Integer> isPrime = x -> {
        if (x < 2) return false;
        for (int i = 2; i <= Math.sqrt(x); ++i) {
            if (x % i == 0) return false;
        }
        return true;
    };

    public static <T> List<T> filter(List<T> list, MyPredicate<T> p) {
        return list.stream().filter(p::test).toList();
    }

}