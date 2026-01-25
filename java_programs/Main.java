import java.util.scanner;

class Solution {
    public void printnumber(scanner sc){

        int number ;
        number = sc.nextInt();
        System.out.println(number);
    }

}

class Main{
    public static void main(string[] args){
        Solution sol = new Solution();
        Scanner sc = new scanner(System.in);
        sol.printnumber(sc);
    }
}
