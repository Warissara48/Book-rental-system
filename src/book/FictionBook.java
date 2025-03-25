package book;

public class FictionBook extends Book {
    private String genre;

    public FictionBook(String title, String category, String genre) {
        super(title, category);
        this.genre = genre;
    }

    @Override
    public String toString() {
        return super.toString() + ", Genre: " + genre;
    }

    @Override
    public String getCategory() {
        return super.getCategory();
    }
}
