package customer;

import java.util.*;

public class Customer {
    private String name;
    private String email;
    private String phoneNumber;
    private Map<String, BorrowedBook> borrowedBooks = new HashMap<>(); // Book Title -> BorrowedBook

    public Customer(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Map<String, BorrowedBook> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void borrowBook(String bookTitle) {
        Date borrowedDate = new Date();
        long millis = 5L * 24 * 60 * 60 * 1000;
        Date dueDate = new Date(borrowedDate.getTime() + millis);
        borrowedBooks.put(bookTitle, new BorrowedBook(bookTitle, borrowedDate, dueDate));
    }

    public int calculateFine(String bookTitle) {
        BorrowedBook borrowedBook = borrowedBooks.get(bookTitle);
        if (borrowedBook != null) {
            long diff = new Date().getTime() - borrowedBook.getDueDate().getTime();
            if (diff > 0) {
                return (int) (diff / (24 * 60 * 60 * 1000)) * 20;
            }
        }
        return 0;
    }

    public void returnBook(String bookTitle) {
        borrowedBooks.remove(bookTitle);
    }

    public class BorrowedBook {
        String title;
        public Date borrowedDate;
        Date dueDate;

        BorrowedBook(String title, Date borrowedDate, Date dueDate) {
            this.title = title;
            this.borrowedDate = borrowedDate;
            this.dueDate = dueDate;
        }

        public Date getDueDate() {
            return dueDate;
        }
    }
}