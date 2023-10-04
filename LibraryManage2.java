import java.util.ArrayList;
import java.util.List;

class Book {
    private int id;
    private String title;
    private String author;
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
    private List<Book> books;
    private List<Member> members;
    private List<List<Integer>> bookTransactions;
    private List<Integer> fines;

    public Library() {
        books = new ArrayList<>();
        members = new ArrayList<>();
        bookTransactions = new ArrayList<>();
        fines = new ArrayList<>();
    }

    public void addBook(int id, String title, String author) {
        books.add(new Book(id, title, author));
    }

    public void registerMember(int memberId, String name) {
        members.add(new Member(memberId, name));
        bookTransactions.add(new ArrayList<>());
        fines.add(0);
    }

    public void removeBook(int bookId) {
        books.removeIf(book -> book.getId() == bookId);
    }

    public void removeMember(int memberId) {
        int index = -1;
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getMemberId() == memberId) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            members.remove(index);
            bookTransactions.remove(index);
            fines.remove(index);
        }
    }

    public Book searchBook(int bookId) {
        for (Book book : books) {
            if (book.getId() == bookId) {
                return book;
            }
        }
        return null;
    }

    public Member searchMember(int memberId) {
        for (Member member : members) {
            if (member.getMemberId() == memberId) {
                return member;
            }
        }
        return null;
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
        Member member = searchMember(memberId);
        Book book = searchBook(bookId);

        if (member != null && book != null && book.isAvailable()) {
            book.setAvailable(false);
            bookTransactions.get(memberId - 1).add(bookId);
        }
    }

    public void returnBook(int memberId, int bookId, int daysLate) {
        Member member = searchMember(memberId);
        Book book = searchBook(bookId);

        if (member != null && book != null && !book.isAvailable()) {
            book.setAvailable(true);
            int fine = calculateFine(daysLate);
            fines.set(memberId - 1, fines.get(memberId - 1) + fine);
            bookTransactions.get(memberId - 1).remove(Integer.valueOf(bookId));
        }
    }

    public void viewLendingInformation(int memberId) {
        List<Integer> transactions = bookTransactions.get(memberId - 1);
        Member member = searchMember(memberId);

        if (transactions.isEmpty() || member == null) {
            System.out.println("No lending information available for this member.");
        } else {
            System.out.println("Lending Information for Member: " + member.getName());
            for (Integer bookId : transactions) {
                Book book = searchBook(bookId);
                System.out.println("Book Title: " + book.getTitle() + ", Author: " + book.getAuthor());
            }
        }
    }

    public void displayOverdueBooks() {
        for (int i = 0; i < members.size(); i++) {
            int memberId = members.get(i).getMemberId();
            int fine = fines.get(i);

            if (fine > 0) {
                Member member = members.get(i);
                List<Integer> transactions = bookTransactions.get(i);

                System.out.println("Member: " + member.getName() + ", Fine: Rs. " + fine);
                System.out.println("Overdue Books:");

                for (Integer bookId : transactions) {
                    Book book = searchBook(bookId);
                    System.out.println("Book Title: " + book.getTitle() + ", Author: " + book.getAuthor());
                }
            }
        }
    }

    private int calculateFine(int daysLate) {
        int fine = 0;

        if (daysLate > 7) {
            fine = 350 + (daysLate - 7) * 100; // Rs. 350 for the first 7 days, Rs. 100 for each additional day
        } else if (daysLate > 0) {
            fine = daysLate * 50; // Rs. 50 per day
        }

        return fine;
    }
}

public class LibraryManage2 {
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
