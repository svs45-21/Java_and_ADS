import java.util.Scanner; 
 

 public class Swap{
    public static void main(String[] args){
        
        Scanner  sc = new Scanner(System.in);
        System.out.println("----Swaping of two numbers----");

        System.out.println("Enter the numbers num1 :");
         int num1 = sc.nextInt();
        System.out.println("Enter the numbers num2 :");
        int num2 = sc.nextInt();
        
        int swap = num1 ;
        num1 = num2;
        num2 = swap;
        System.out.println("After swapping the number is :" + num1 + " and "  +num2);

    }

}