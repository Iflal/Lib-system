import java.util.*;

//class Book
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

// class Member
class Member {
    private int memberId;
    private String name;

    // member constractor set
    public Member(int memberId, String name) {
        this.memberId = memberId;
        this.name = name;
    }

    public int getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }
}

// class Libaray
class Library {

    private List<Book> books;// create list for Book class
    private List<Member> members;// create list for Member class
    private List<List<Integer>> bookTrans;
    private List<Integer> fines;

    // create ArrayList inside the library constractor
    public Library() {
        books = new ArrayList<>();
        members = new ArrayList<>();
        bookTrans = new ArrayList<>();
        fines = new ArrayList<>();

    }

    public void addBook(int id, String title, String author) {
        books.add(new Book(id, title, author));
    }

    public void registerMember(int memberId, String name) {
        members.add(new Member(memberId, name));
        bookTrans.add(new ArrayList<>());// how many transaction happen
        fines.add(0);
    }

    public void removeBook(int bookId) {
        // check the bookId that provide here.
        // If its here it will remove using "removeIf" function.
        books.removeIf((book) -> {
            return book.getID() == bookId;
        });
    }

    public void removeMember(int memberId) {
        int index = -1;

        // find the member index
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getMemberId() == memberId) {
                index = i;
                break;
            }

            // remove the member from the index
            if (index != -1) {
                members.remove(index);
                bookTrans.remove(index);
                fines.remove(index);
            }
        }

    }

    // find book using Book Id
    public Book searchBook(int bookId) {
        for (Book book : books) {
            if (book.getID() == bookId) {
                return book;
            }
        }
        return null; // not found
    }

    // find book using Member Id
    public Member searchMember(int memberId) {
        for (Member member : members) {
            if (member.getMemberId() == memberId) {
                return member;
            }
        }
        return null; // not found
    }

    public List<String> displayBookNames() {
        List<String> bookNames = new ArrayList<>();
        for (Book book : books) {
            bookNames.add(book.getTitle());
        }
        return bookNames;
    }

    public List<String> displayMemberNames() {
        List<String> memberNames = new ArrayList<>();
        for (Member member : members) {
            memberNames.add(member.getName());
        }
        return memberNames;
    }

    public void lendBook(int memberId, int bookId) {
        Member member = searchMember(memberId);// find the member and assign
        Book book = searchBook(bookId); // find the book and assign

        if (member != null && book != null && book.isAvailable()){
            book.setAvailable(false);
            bookTrans.get(memberId - 1).add(bookId);
        }
    }

    public void returnBook(int memberId, int bookId, int dayslate){
        Member member = searchMember(memberId);
        Book book = searchBook(bookId);

        if(member != null && book != null&& !book.isAvailable()){
            book.setAvailable(true);
            bookTrans.get(memberId -1).remove(Integer.valueOf(bookId));
        }
    }

    public void viewLendingInformation(int memberId){
        List<Integer> trans = bookTrans.get(memberId -1);
        Member member = searchMember(memberId);

        //check the trans value
        //if there is no such Id member is not lend the "trans" list is empty
        if(trans.isEmpty() || member ==null){
            System.out.println("No lending information available for this member");
        }else{
            System.out.println("Lending Information for Member: " + member.getName());
            for(Integer bookId: trans){
                Book book = searchBook(bookId);// fine the book and assign to book object list
                System.out.println("Book Title: " + book.getTitle() + " ,Author: " + book.getAuthor() );
            }
        }

    }

    public void displayOverdueBooks(){}


}

public class Librarytest {

}
