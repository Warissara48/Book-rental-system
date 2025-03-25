package book;

public class NonFictionBook extends Book {
    private String subject;

    public NonFictionBook(String title, String category, String subject) {
        super(title, category);
        this.subject = subject;
    }

    @Override
    public String toString() {
        return super.toString() + ", Subject: " + subject;
    }

    @Override
    public String getCategory() {
        return super.getCategory();
    }
}