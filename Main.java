class Person {
   private String name;

   // Getter
   public String getName() {
     return name;
   }

   // Setter
   public void setName(String newName) {
     this.name = newName;
   }
}

public class Main{
    public static void main(String[] args){
        Person p1=new Person();
        
        p1.setName("Wasri");

        System.out.println(p1.getName());

    }
}