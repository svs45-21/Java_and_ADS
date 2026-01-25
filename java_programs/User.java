import java.util.Scanner;
public class User {
    public static void main(String[] args) {
        System.out.println("Enter the first number:");
        Scanner s = new Scanner(System.in);
        int num1 = s.nextInt();
        float num3 = s.nextFloat();
        System.out.println("Enter the second number:");
        int num2 = s.nextInt();
        float num4 = s.nextFloat();
        int sum = num1 + num2;
        float sum2 = num3 + num4;
        System.out.println("The sum is: " + sum);
        System.out.println("The sum of floats is: " + sum2);
    }
    
}
