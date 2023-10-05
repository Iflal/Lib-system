import java.text.SimpleDateFormat;
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

class Transaction {
    private int bookId;
    private int memberId;
    private Date returnDate;
    private Date borrowedDate;
    private Date dueDate;

    public Transaction(int bookId, int memberId, Date returnDate, Date borrowedDate, Date dueDate) {
        this.bookId = bookId;
        this.memberId = memberId;
        this.returnDate = returnDate;
        this.borrowedDate = borrowedDate;
        this.dueDate = dueDate;
    }

    public int getBookId() {
        return bookId;
    }

    public int getMemberId() {
        return memberId;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public Date getBorrowedDate() {
        return borrowedDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }
}

// class Libaray
class Library {

    private List<Book> books;// create list for Book class
    private List<Member> members;// create list for Member class
    private List<Transaction> bookTrans;
    private List<Integer> fines;

    // create ArrayList inside the library constractor
    public Library() {
        books = new ArrayList<>();
        members = new ArrayList<>();
        bookTrans = new ArrayList<>();
        fines = new ArrayList<>();

    }

    // get members list
    public List<Member> getMembers() {
        return members;
    }

    public void addBook(int id, String title, String author) {
        books.add(new Book(id, title, author));
    }

    public void registerMember(int memberId, String name) {
        members.add(new Member(memberId, name));
        fines.add(0);
    }

    public void removeBook(String bookTitle) {
        // check the book name that provide here.
        // If its here it will remove using "removeIf" function.
        books.removeIf((book) -> book.getTitle().equalsIgnoreCase(bookTitle));

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

    public Book searchBookByName(String bName) {
        for (Book book : books) {
            if (book.getTitle() == bName) {
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

    public Member searchMemberByName(String name) {
        for (Member member : members) {
            if (member.getName() == name) {
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

        if (member != null && book != null && book.isAvailable()) {
            book.setAvailable(false);
            Date borrowedDate = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(borrowedDate);
            calendar.add(Calendar.DAY_OF_MONTH, 14); // Due date is 2 weeks from borrowing
            Date dueDate = calendar.getTime();

            // here we initially set null for return date.

            /*
             * because in the Transaction object since the book hasn't been returned at the
             * time of lending.
             */
            bookTrans.add(new Transaction(bookId, memberId, null, borrowedDate, dueDate));
        }
    }

    public void returnBook(int memberId, int bookId) {
        Member member = searchMember(memberId);
        Book book = searchBook(bookId);

        if (member != null && book != null && !book.isAvailable()) {
            book.setAvailable(true);
            Date returnDate = new Date();
            Transaction transaction = findTransaction(memberId, bookId);
            if (transaction != null) {
                transaction.setReturnDate(returnDate);
                int daysLate = calculateDaysLate(transaction.getDueDate(), returnDate);
                int fine = calculateFine(daysLate);
                fines.set(memberId - 1, fines.get(memberId - 1) + fine);
            }
        }
    }

    public void viewLendingInformation() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        System.out.println("Lending Information:");
        for (Transaction transaction : bookTrans) {
            int bookId = transaction.getBookId();
            Book book = searchBook(bookId);
            int memberId = transaction.getMemberId();
            Member member = searchMember(memberId);
            Date borrowedDate = transaction.getBorrowedDate();
            Date dueDate = transaction.getDueDate();
            Date returnDate = transaction.getReturnDate();

            System.out.println("Member: " + member.getName());
            System.out.println("Book: " + book.getTitle() + ", Author: " + book.getAuthor());
            System.out.println("Borrowed Date: " + dateFormat.format(borrowedDate));
            System.out.println("Due Date: " + dateFormat.format(dueDate));

            if (returnDate != null) {
                System.out.println("Return Date: " + dateFormat.format(returnDate));
                int daysLate = calculateDaysLate(dueDate, returnDate);
                int fine = calculateFine(daysLate);
                System.out.println("Fine: Rs. " + fine);
            }

            System.out.println();
        }
    }

    public void displayOverdueBooks() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// java text package simple date formate
        Date currentDate = new Date();

        for (Transaction transaction : bookTrans) {
            if (transaction.getReturnDate() == null) {
                int memberId = transaction.getMemberId();
                Member member = searchMember(memberId);
                int bookId = transaction.getBookId();
                Book book = searchBook(bookId);

                if (book != null && member != null) {
                    Date dueDate = transaction.getDueDate();

                    // check due date before the current date
                    if (dueDate.before(currentDate)) {
                        System.out.println("Member: " + member.getName());
                        System.out.println("Overdue Book:");
                        System.out.println("Book Title: " + book.getTitle() +
                                ", Author: " + book.getAuthor() +
                                ", Due Date: " + dateFormat.format(dueDate));
                    }
                }
            }
        }
    }

    // ================================================
    // Calculate functions

    private Transaction findTransaction(int memberId, int bookId) {
        for (Transaction transaction : bookTrans) {
            if (transaction.getMemberId() == memberId && transaction.getBookId() == bookId) {
                return transaction;
            }
        }
        return null;
    }

    private int calculateDaysLate(Date dueDate, Date returnDate) {
        long diff = returnDate.getTime() - dueDate.getTime();
        return (int) (diff / (24 * 60 * 60 * 1000)); // Convert milliseconds to days
    }

    public int calculateFine(int days) {
        int fine = 0;

        if (days > 7) {
            fine = 350 + (days - 7) * 100;
        } else if (days > 0) {
            fine = days * 50;
        }

        return fine;
    }
    // ========================================================
}

// --
// ----
// ==============Main Class===========
public class Librarytest {

    public static void main(String[] args) {
        Library lib = new Library();
        Scanner scanner = new Scanner(System.in);
        int currentBookId = 1; // initialize the book Id

        boolean exit = false;// initlize the exit flag

        while (!exit) {

            // ==============
            // PRINT THE MENU
            System.out.println("========Library System Managment========\n\n\n");
            System.out.println(" Select Choice of Function:\n");
            System.out.println("1. Add Books \n2. Remove Book \n3. Search Book Information \n4. Display Book Names ");
            System.out.println(
                    "5.Lend a Book \n6.Return a Book \n7.View Lending Infromation \n8. Display Overdue Books \n9.Fine Calculation");
            System.out.println(
                    "\n--Member Related--\n \n10.Register Member \n11. Remove Member \n12.Search Member Information \n13. Display Member Names");
            System.out.println("14. Quit");
            // ===============

            // USER CHOICE
            System.out.println("\nEnter your choice: ");
            int choice = scanner.nextInt();

            // =================== choices
            switch (choice) {
                case 1:
                    scanner.nextLine();
                    System.out.println("Enter the book title:");
                    String title = scanner.nextLine();
                    System.out.println("Enter the book author: ");
                    String author = scanner.nextLine();

                    lib.addBook(currentBookId, title, author);
                    currentBookId++;
                    break;

                case 2:
                    scanner.nextLine();
                    System.out.println("Enter the book title:");
                    String rmTitle = scanner.nextLine();
                    lib.removeBook(rmTitle);
                    System.out.println(rmTitle + "Successfully removed");
                    break;

                case 3:
                    scanner.nextLine();
                    System.out.println("Enter the book title:");
                    String S_Title = scanner.nextLine();
                    Book searchBook = lib.searchBookByName(S_Title);

                    if (searchBook != null) {
                        System.out.println("Book Found: " + searchBook.getTitle() + " by " + searchBook.getAuthor()
                                + "\nBook ID: " + searchBook.getID() + "\nAvailability: " + searchBook.isAvailable());

                    } else {
                        System.out.println("Book not found.");
                    }
                    break;
                case 4:
                    scanner.nextLine();
                    List<String> bookNames = lib.displayBookNames();
                    if (bookNames.isEmpty()) {
                        System.out.println("No books in the library.");
                    } else {
                        System.out.println("Book Names:");
                        for (String bookName : bookNames) {
                            System.out.println(bookName);
                        }
                    }
                    break;

                case 5:
                    scanner.nextLine();

                    System.out.print("Enter member ID: ");
                    int lendMemberId = scanner.nextInt();
                    System.out.print("Enter book ID: ");
                    int lendBookId = scanner.nextInt();
                    lib.lendBook(lendMemberId, lendBookId);
                    break;

                case 6:
                    scanner.nextLine();

                    System.out.print("Enter member ID: ");
                    int returnMemberId = scanner.nextInt();
                    System.out.print("Enter book ID: ");
                    int returnBookId = scanner.nextInt();
                    lib.returnBook(returnMemberId, returnBookId);
                    break;

                case 7:
                    lib.viewLendingInformation();
                    break;

                case 8:
                    lib.displayOverdueBooks();
                    break;

                case 9:
                    scanner.nextLine();
                    System.out.print("Enter how many date late: ");
                    int days = scanner.nextInt();
                    int fine = lib.calculateFine(days);
                    System.out.println("Fine amount: Rs. " + fine);
                    break;

                case 10:
                    scanner.nextLine();
                    System.out.print("Enter member name: ");
                    String memberName = scanner.nextLine();
                    lib.registerMember(lib.getMembers().size() + 1, memberName);
                    break;

                case 11:
                    scanner.nextLine();
                    System.out.print("Enter member ID to remove: ");
                    int memberIdToRemove = scanner.nextInt();
                    lib.removeMember(memberIdToRemove);
                    break;

                case 12:
                    scanner.nextLine();
                    System.out.print("Enter member name to search: ");
                    String name = scanner.nextLine();
                    Member foundMember = lib.searchMemberByName(name);
                    if (foundMember != null) {
                        System.out.println(
                                "Member Found: " + foundMember.getName() + "\nMember ID: " + foundMember.getMemberId());
                    } else {
                        System.out.println("Member not found.");
                    }
                    break;

                case 13:
                    List<String> memberNames = lib.displayMemberNames();
                    if (memberNames.isEmpty()) {
                        System.out.println("No registered members in the library.");
                    } else {
                        System.out.println("Member Names:");
                        for (String m_Names : memberNames) {
                            System.out.println(m_Names);
                        }
                    }
                    break;
                // ======================

                // -----------------EXIT----------------
                case 14:
                    exit = true;
                default:
                    System.out.println("Invalid choice. please try Again");
            }
        }
        scanner.close();
    }
}
