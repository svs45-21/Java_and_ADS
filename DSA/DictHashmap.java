import java.util.HashMap;
import java.util.Map;
class Hd{
    HashMap<Integer, String> obj = new HashMap<>();

    //Insert or Upadate 
    void Insert(int rollNumber, String Name){
        obj.put(rollNumber, Name);
    }

    //Search
    String Search(int rollNumber){
        return obj.get(rollNumber);
    }

    //Delete

    void Delete(int rollNumber){
         obj.remove(rollNumber);
    }

    //Display
    
    void Display(){
          for (Map.Entry<Integer, String> element : obj.entrySet()) {
        System.out.println("Roll number = " + element.getKey() +
                           ", Student name = " + element.getValue());
        }
    }
}

public class DictHashmap{
    public static void main(String[] args){

        Hd h = new Hd();

        h.Insert(101,"ganga");
        h.Insert(102,"kiran");
        h.Insert(103,"shree");
        
        h.Display();
        System.out.println("Afeter Inserting the key and value");
        String Result = h.Search(102);
        System.out.println("The search Result :" +Result);
        
        h.Delete(103);
        h.Display();
       

        //modifing or update
        h.Insert(102,"Shree");

        h.Display();
    }
}
