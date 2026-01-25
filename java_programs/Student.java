import java.util.Scanner;

public class Student{
    public static void main(String[] args) {
        System.out.println("Enter the student name:");
        Scanner s = new Scanner(System.in);
        String name = s.nextLine();
        System.out.println("Enter the class of the student:");
        String Class = s.nextLine(); 
        System.out.println("Enter the roll number of the student:");
        int rollnumber = s.nextInt();
        System.out.println("Enter the 5 subject marks of the student:");
        int marks1 = s.nextInt();
        int marks2 = s.nextInt();       
        int marks3 = s.nextInt();
        int marks4 = s.nextInt();
        int marks5 = s.nextInt();
        int totalmarks = marks1 + marks2 + marks3 + marks4 + marks5;
        float percentage = (totalmarks / 500.0f) * 100;
        System.out.println("Student Name: " + name);
        System.out.println("Class: " + Class);
        System.out.println("Roll Number: " + rollnumber);
        System.out.println("Total Marks: " + totalmarks);
        System.out.println("Percentage: " + percentage + "%");
       
    }
}