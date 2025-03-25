import book.Book;
import book.FictionBook;
import book.NonFictionBook;
import customer.Customer;
import customer.PremiumCustomer;
import customer.RegularCustomer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BookRentalGUI {
    JFrame frame;
    ArrayList<Book> books = new ArrayList<>();
    ArrayList<Customer> customers = new ArrayList<>();
    Customer currentCustomer;

    public BookRentalGUI() {
        frame = new JFrame("Book Rental System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        books.add(new Book("Harry Potter", "Fantasy"));
        books.add(new Book("The Hobbit", "Fantasy"));
        books.add(new Book("Rich Dad Poor Dad", "Finance"));
        books.add(new Book("The Millionaire Next Door", "Finance"));
        books.add(new Book("Your Money or Your Life", "Finance"));
        books.add(new Book("Clean Code", "Programming"));
        books.add(new Book("Python Crash Course", "Programming"));
        books.add(new Book("The Pragmatic Programmer", "Programming"));

        customers.add(new PremiumCustomer("Jane", "jane@example.com", "123456789", 0.1));
        customers.add(new RegularCustomer("Bob", "bob@example.com", "987654321"));

        mainMenu();
    }

    public void mainMenu() {
        String[] options = {"Admin", "User"};
        int choice = JOptionPane.showOptionDialog(frame, "Are you an Admin or User?",
                "Login", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);

        if (choice == 0) {
            adminLogin();
        } else if (choice == 1) {
            customerLogin();
        }

        frame.setVisible(true);
    }

    public void adminLogin() {
        String username = JOptionPane.showInputDialog(frame, "Enter Admin Username:");
        String password = JOptionPane.showInputDialog(frame, "Enter Admin Password:");

        Admin admin = new Admin();

        if (admin.validateCredentials(username, password)) {
            JOptionPane.showMessageDialog(frame, "Admin logged in successfully.");
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            AuditLog.addLog("Alice" + " - " + "Logged in as Admin " , timestamp);
            showAdminMenu();
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid credentials. Please try again.");
        }
    }

    public void showAdminMenu() {
        frame.getContentPane().removeAll();
        frame.setSize(400, 300);
        JPanel panel = new JPanel(new GridLayout(5, 1));

        JButton addBookBtn = new JButton("Add a Book");
        JButton deleteBookBtn = new JButton("Delete a Book");
        JButton viewCustomerBtn = new JButton("View Customer Information");
        JButton viewAuditLogBtn = new JButton("View Audit Log");
        JButton backBtn = new JButton("Return to Main Menu");

        addBookBtn.addActionListener(e -> addBook());
        deleteBookBtn.addActionListener(e -> deleteBook());
        viewCustomerBtn.addActionListener(e -> viewCustomerInformation());
        viewAuditLogBtn.addActionListener(e -> viewAuditLog());
        backBtn.addActionListener(e -> mainMenu());

        panel.add(addBookBtn);
        panel.add(deleteBookBtn);
        panel.add(viewCustomerBtn);
        panel.add(viewAuditLogBtn);
        panel.add(backBtn);

        frame.add(panel);
        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }


    public void addBook() {
        String[] bookTypes = {"Fiction", "Non-Fiction"};
        String bookType = (String) JOptionPane.showInputDialog(frame, "Select book type", "Choose Book Type", JOptionPane.QUESTION_MESSAGE, null, bookTypes, bookTypes[0]);

        if (bookType != null) {
            String bookTitle = JOptionPane.showInputDialog(frame, "Enter the name of the book:");

            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            AuditLog.addLog("Alice" + " - " + "Added book: " + bookTitle , timestamp);

            if (bookTitle != null && !bookTitle.isEmpty()) {
                if (bookType.equals("Fiction")) {
                    books.add(new FictionBook(bookTitle, "Fiction", "Fantasy"));
                } else if (bookType.equals("Non-Fiction")) {
                    books.add(new NonFictionBook(bookTitle, "Non-Fiction", "History"));
                }
                JOptionPane.showMessageDialog(frame, "Book added successfully.");
            } else {
                JOptionPane.showMessageDialog(frame, "Book title cannot be empty.");
            }
        }
    }


    public void deleteBook() {
        if (books.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No books available to delete.");
            return;
        }

        String[] bookTitles = books.stream().map(Book::getTitle).toArray(String[]::new);
        String bookToDelete = (String) JOptionPane.showInputDialog(frame, "Select a book to delete", "Delete Book", JOptionPane.QUESTION_MESSAGE, null, bookTitles, bookTitles[0]);

        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        AuditLog.addLog("Alice" + " - "  + "Delete book: " + bookToDelete , timestamp);

        if (bookToDelete != null) {
            books.removeIf(book -> book.getTitle().equals(bookToDelete));
            JOptionPane.showMessageDialog(frame, "Book deleted successfully.");
        }
    }


    public void viewCustomerInformation() {
        if (customers.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No customers available.");
            return;
        }

        String[] customerNames = customers.stream().map(Customer::getName).toArray(String[]::new);
        String customerName = (String) JOptionPane.showInputDialog(frame, "Select a customer", "View Customer Information", JOptionPane.QUESTION_MESSAGE, null, customerNames, customerNames[0]);

        if (customerName != null) {
            Customer selectedCustomer = null;
            for (Customer customer : customers) {
                if (customer.getName().equals(customerName)) {
                    selectedCustomer = customer;
                    break;
                }
            }

            if (selectedCustomer != null) {
                StringBuilder customerInfo = new StringBuilder();
                customerInfo.append("Name: ").append(selectedCustomer.getName())
                        .append("\nEmail: ").append(selectedCustomer.getEmail())
                        .append("\nPhone: ").append(selectedCustomer.getPhoneNumber())
                        .append("\n\nBorrowed Books:\n");

                selectedCustomer.getBorrowedBooks().forEach((title, borrowedBook) -> {
                    customerInfo.append("Book: ").append(title)
                            .append("\nBorrowed Date: ").append(new SimpleDateFormat("yyyy-MM-dd").format(borrowedBook.borrowedDate))
                            .append("\nDue Date: ").append(new SimpleDateFormat("yyyy-MM-dd").format(borrowedBook.getDueDate()))
                            .append("\n\n");
                });

                String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                AuditLog.addLog(currentCustomer.getName() + " - " + "View Customer Information" , timestamp);
                JOptionPane.showMessageDialog(frame, customerInfo.toString());
            }
        }
    }


    public void viewAuditLog() {
        String logs = AuditLog.getLogs();
        if (logs.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No audit logs available.");
        } else {
            JOptionPane.showMessageDialog(frame, logs);
        }
    }

    public void customerLogin() {
        String name = JOptionPane.showInputDialog(frame, "Enter your name:");
        String email = JOptionPane.showInputDialog(frame, "Enter your email:");

        if (name != null && email != null) {
            currentCustomer = null;
            for (Customer customer : customers) {
                if (customer.getName().equals(name) && customer.getEmail().equals(email)) {
                    currentCustomer = customer;
                    break;
                }
            }

            if (currentCustomer != null) {
                String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                AuditLog.addLog(currentCustomer.getName() + " - " + "Logged in as Customer " , timestamp);

                if (currentCustomer instanceof PremiumCustomer) {
                    PremiumCustomer premiumCustomer = (PremiumCustomer) currentCustomer;
                    JOptionPane.showMessageDialog(frame, "You are a Premium Member! Enjoy your benefits.\nYour discount rate is: "
                            + premiumCustomer.getDiscountRate() * 100 + "% on book rentals.");
                } else {
                    JOptionPane.showMessageDialog(frame, "You are a Regular Member.\nEnjoy your rental experience.");
                }

                customerMenu();
            } else {
                JOptionPane.showMessageDialog(frame, "Customer not found. Please check your credentials.");
            }
        }
    }

    public void customerMenu() {
        frame.getContentPane().removeAll();
        frame.setSize(300, 200);
        JPanel panel = new JPanel(new GridLayout(3, 1));

        JButton customerBtn = new JButton("Borrow a Book");
        JButton customerReturnBtn = new JButton("Return the Book");
        JButton backBtn = new JButton("Return to Main Menu");

        customerBtn.addActionListener(e -> selectCategory());
        customerReturnBtn.addActionListener(e -> customerReturnBook());
        backBtn.addActionListener(e -> mainMenu());

        panel.add(customerBtn);
        panel.add(customerReturnBtn);
        panel.add(backBtn);

        frame.add(panel);
        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }

    public void selectCategory() {
        ArrayList<String> categories = new ArrayList<>();
        for (Book book : books) {
            String bookCategory = book.getCategory();
            if (bookCategory != null && !categories.contains(bookCategory)) {
                categories.add(bookCategory);
            }
        }

        if (categories.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No categories available.");
            return;
        }

        String category = (String) JOptionPane.showInputDialog(frame, "Select a category", "Choose Category", JOptionPane.QUESTION_MESSAGE, null, categories.toArray(), categories.get(0));
        if (category != null) {
            selectBook(category);
        }
    }

    private void selectBook(String category) {
        ArrayList<Book> categoryBooks = new ArrayList<>();
        if (category == null || category.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Invalid category selected.");
            return;
        }

        for (Book book : books) {
            if (book.getCategory() != null && book.getCategory().equals(category)) {
                categoryBooks.add(book);
            }
        }

        if (categoryBooks.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No books found in this category.");
            return;
        }

        String[] bookTitles = categoryBooks.stream().map(Book::getTitle).toArray(String[]::new);
        String bookTitle = (String) JOptionPane.showInputDialog(frame, "Select a book", "Choose Book", JOptionPane.QUESTION_MESSAGE, null, bookTitles, bookTitles[0]);

        if (bookTitle != null) {
            borrowBook(bookTitle);
        }
    }

    private void borrowBook(String bookTitle) {
        currentCustomer.borrowBook(bookTitle);
        JOptionPane.showMessageDialog(frame, "You have borrowed: " + bookTitle);

        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Customer.BorrowedBook borrowedBook = currentCustomer.getBorrowedBooks().get(bookTitle);

        if (borrowedBook != null) {
            Date dueDate = borrowedBook.getDueDate();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDueDate = dateFormat.format(dueDate);

            double originalFee = 30.0;
            double finalFee = originalFee;

            if (currentCustomer instanceof PremiumCustomer) {
                finalFee = ((PremiumCustomer) currentCustomer).applyDiscount(originalFee);
            }

            String borrowedDetails = "Book borrowed: " + bookTitle + "\nDue Date: " + formattedDueDate
                    + "\nRental Fee: " + finalFee + " baht";
            JOptionPane.showMessageDialog(frame, borrowedDetails);

            AuditLog.addLog(currentCustomer.getName() + " - " + "Borrowed book: " + bookTitle + ". Due Date: "
                    + formattedDueDate + ". Rental Fee: " + finalFee + " baht", timestamp);
        } else {
            JOptionPane.showMessageDialog(frame, "Error: Book borrowing failed.");
        }
    }


    private void customerReturnBook() {
        if (currentCustomer.getBorrowedBooks().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "You have no borrowed books.");
            return;
        }

        String[] borrowedBooks = currentCustomer.getBorrowedBooks().keySet().toArray(new String[0]);
        String bookToReturn = (String) JOptionPane.showInputDialog(frame, "Select a book to return", "Return Book", JOptionPane.QUESTION_MESSAGE, null, borrowedBooks, borrowedBooks[0]);

        if (bookToReturn != null) {
            int fine = currentCustomer.calculateFine(bookToReturn);
            String fineMessage = fine > 0 ? "Fine: " + fine + " baht" : "No fine";
            int confirm = JOptionPane.showConfirmDialog(frame, "Do you want to return the book: " + bookToReturn + "?\n" + fineMessage, "Confirm Return", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                currentCustomer.returnBook(bookToReturn);
                JOptionPane.showMessageDialog(frame, "You have successfully returned the book: " + bookToReturn + "\n" + fineMessage);
                String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                AuditLog.addLog(currentCustomer.getName() + " - " + "Returned book: " + bookToReturn + ". Fine: " + fine + " baht", timestamp);
            }
        }
    }


    public static void main(String[] args) {
        new BookRentalGUI();
    }
}