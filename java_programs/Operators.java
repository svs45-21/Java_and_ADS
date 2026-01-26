public calss Operators{
    public static void main(String[] args){

        //Assignment operator
        //example (=)
        int num1 = 20; // syntax is datatype variable = litreal; where = is the assignment operator

        //Arithmetic operators
        //examples (+, -, *, /, %)
        int sum = num1 + 10; // addition
        int difference = num1 - 5; // subtraction
        int product = num1 * 2; // multiplication
        int quotient = num1 / 4; // division
        int remainder = num1 % 3; // modulus  
        System.out.println("Sum: " + sum);
        System.out.println("Difference: " + difference);
        System.out.println("Product: " + product);  
        System.out.println("Quotient: " + quotient);
        System.out.println("Remainder: " + remainder);

        //floating Number arithmetic
        double num2 = 15.5;
        double floatSum = num2 + 4.5;
        System.out.println("Floating Point Sum: " + floatSum);  
        System.out.println("num2 : " + num2);
         
    }
}