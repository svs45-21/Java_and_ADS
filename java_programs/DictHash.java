import java.util.HashMap;
import java.util.Map;

class Hd{
    HashMap<Integer, String> obj = new HashMap<>();

    //Insert or Update
    void Insert(int RollNo,String Name){
        obj.put(RollNo, Name);
    }

    //search 
    String Search(int RollNo){
        return obj.get(RollNo);
    }

    //Delete
    void Delete(int RollNo){
        obj.remove(RollNo);
    }

    //Display
    void Display(){
        for(Map.Entry<Integer, String> entry : obj.entrySet()){
            System.out.println("Roll No: " + entry.getKey() + ", Name: " + entry.getValue());
        }
    }
}

public class DictHash{
    public static void main(String[] args) {
        Hd h = new Hd();

        h.Insert (1 ,"Rahul");
        h.Insert(2,"ranjan");
        h.Insert(3,"tiger");

        h.Display();

        String result = h.Search(2);
        System.out.println("Search Result :" + result);

        h.Delete(2);
        h.Display();

        //Update
        h.Insert(3,"Ajay");
        h.Display();

    }
}