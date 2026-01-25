public class DataTypes {
    public static void main(String[] args) {

        // Primitive Data Types
        int i = 10;
        float f = 10.5f;
        double d = 20.99;
        char c = 'A';
        boolean boolValue = true;
        byte b = 10;
        short s =100;
        long l = 100000000000l;
        
        // Non-Primitive Data Types
        String stringValue = "Hello, World!";
        int[] arr = {1, 2, 3, 4, 5};
        Integer WrapperInt = Integer.valueOf(50);
        StringBuilder sb = new StringBuilder("String Builder Example");

        // Output 
        System.out.println("Primitive Data Types:");
        System.out.println("Int: " + i);
        System.out.println("Float: " + f);
        System.out.println("Double: " + d);
        System.out.println("Character: " + c);
        System.out.println("Boolean: " + boolValue);
        System.out.println("Byte: " + b);
        System.out.println("Short: " + s);
        System.out.println("Long: " + l);
        System.out.println("\nNon-Primitive Data Types:");
        System.out.println("String: " + stringValue);
        System.out.print("Array: ");
        for(int num : arr) {
            System.out.print(num + " ");                                
        }
        System.out.println("\nWrapper Class Integer: " + WrapperInt);
        System.out.println("StringBuilder: " + sb.toString());  
    }
}
