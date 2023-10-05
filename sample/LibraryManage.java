package sample;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Book {
    private String title;
    private String author;
    private int id;
    private boolean isAvailable;

    public Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isAvailable = true;
    }

    public int getId() {
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

class Library {
    private Map<Integer, Book> books;
    private Map<Integer, Member> members;
    private Map<Integer, List<Integer>> bookTransactions;
    private Map<Integer, Integer> fines;

    public Library() {
        books = new HashMap<>();
        members = new HashMap<>();
        bookTransactions = new HashMap<>();
        fines = new HashMap<>();
    }

    public void addBook(int id, String title, String author) {
        books.put(id, new Book(id, title, author));
    }

    public void registerMember(int memberId, String name) {
        members.put(memberId, new Member(memberId, name));
    }

    public void removeBook(int bookId) {
        books.remove(bookId);
    }

    public void removeMember(int memberId) {
        members.remove(memberId);
    }

    public Book searchBook(int bookId) {
        return books.get(bookId);
    }

    public Member searchMember(int memberId) {
        return members.get(memberId);
    }

    public List<String> displayBookNames() {
        List<String> bookNames = new ArrayList<>();
        for (Book book : books.values()) {
            bookNames.add(book.getTitle());
        }
        return bookNames;
    }

    public List<String> displayMemberNames() {
        List<String> memberNames = new ArrayList<>();
        for (Member member : members.values()) {
            memberNames.add(member.getName());
        }
        return memberNames;
    }

    public void lendBook(int memberId, int bookId) {
        Book book = books.get(bookId);
        Member member = members.get(memberId);

        if (book != null && member != null && book.isAvailable()) {
            book.setAvailable(false);
            List<Integer> transactions = bookTransactions.getOrDefault(memberId, new ArrayList<>());
            transactions.add(bookId);
            bookTransactions.put(memberId, transactions);
        }
    }

    public void returnBook(int memberId, int bookId, int daysLate) {
        Book book = books.get(bookId);
        Member member = members.get(memberId);

        if (book != null && member != null && !book.isAvailable()) {
            book.setAvailable(true);

            int fine = 0;
            if (daysLate > 7) {
                fine = (daysLate - 7) * 100 + 350; // Rs. 350 for the first 7 days
            } else if (daysLate > 0) {
                fine = daysLate * 50;
            }

            fines.put(memberId, fines.getOrDefault(memberId, 0) + fine);

            List<Integer> transactions = bookTransactions.getOrDefault(memberId, new ArrayList<>());
            transactions.remove(Integer.valueOf(bookId)); // Remove the book from transactions
            bookTransactions.put(memberId, transactions);
        }
    }

    public void viewLendingInformation(int memberId) {
        List<Integer> transactions = bookTransactions.get(memberId);
        if (transactions != null) {
            for (int bookId : transactions) {
                Book book = books.get(bookId);
                System.out.println("Book Title: " + book.getTitle() + ", Author: " + book.getAuthor());
            }
        }
    }

    public void displayOverdueBooks() {
        for (int memberId : fines.keySet()) {
            int fine = fines.get(memberId);
            if (fine > 0) {
                Member member = members.get(memberId);
                List<Integer> transactions = bookTransactions.get(memberId);
                System.out.println("Member: " + member.getName() + ", Fine: Rs. " + fine);
                System.out.println("Overdue Books:");
                for (int bookId : transactions) {
                    Book book = books.get(bookId);
                    System.out.println("Book Title: " + book.getTitle() + ", Author: " + book.getAuthor());
                }
            }
        }
    }
}

public class LibraryManage{
    public static void main(String[] args) {
        Library library = new Library();

        library.addBook(1, "Book1", "Author1");
        library.addBook(2, "Book2", "Author2");
        library.addBook(3, "Book3", "Author3");

        library.registerMember(101, "Member1");
        library.registerMember(102, "Member2");

        library.lendBook(101, 1);
        library.lendBook(101, 2);

        library.returnBook(101, 1, 10);
        library.returnBook(101, 2, 5);

        library.viewLendingInformation(101);
        library.displayOverdueBooks();
    }
}
