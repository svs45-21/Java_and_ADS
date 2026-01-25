import java.util.Scanner;

public class Add{
     
     public static void main(String[] args){
        /*
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your name  : ");
        String Name = sc.nextLine();
        System.out.println("Welcom to the class " + Name + " Enjoy the day dear");*/

        Scanner sc =  new Scanner(System.in);
        System.out.println(" Enter the two Numbers NUm1 and Num2 : " );
        int num1 = sc.nextInt();
        int num2 = sc.nextInt();
        int result = num1 + num2;
        System.out.println("The Addition of two numbers is " + num1 + " and "  + num2  + " is :" +result);
     }   
}