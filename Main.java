abstract class Main {
    public String fname = "Jhon";
    public int age = 24;

    public abstract void study();
}

class Student extends Main {
    public int Graduate = 2018;

    public void study() {
        System.out.println("Studying all day long");
    }
}
