class Book {
    private String title;
    private String author;
    private int id;
    private boolean isAvailable;

    // book constractor
    public Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isAvailable = true;
    }

    public int getID() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}

class Member {
    private int memberId;
    private String name;

    //member constractor set
    public Member(int memberId, String name) {
        this.memberId = memberId;
        this.name = name;
    }

    public int getMemberId(){
        return memberId;
    }

    public String getName(){
        return name;
    }
}

class Library{
    
} 

public class Librarytest {

}
