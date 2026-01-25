

public class Operator{
    public static void main(String[] args){
        int a  = 10;
        int b = 20;
        int sum =a + b;
        System.out.println("Sum: " + sum);
        int diff = b - a;
        System.out.println("Difference: " + diff);
        int mul = a * b;
        System.out.println("Multiplication: " + mul);
        int div = b / a;
        System.out.println("Division: " + div); 
        int quot = b % a;
        System.out.println("Remainder: " + quot);
        int bool = (a < b) ? a : b;
        System.out.println("Larger value: " + bool);    
        System.out.println(a > b && b > a); //logical and operator 
        System.out.println(a > b || b > a); //logical or operator
        System.out.println(!(a > b)); //ogical not operator
       }
}