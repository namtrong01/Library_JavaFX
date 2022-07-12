package Model;

public class Books {
    private int id;
    private String title;
    private String author;
    private int year;
    private int pages;
    private boolean available;


    public Books (int Id, String Title, String Author, int Year, int Pages, boolean Available){
        this.author= Author;
        this.id=Id;
        this.pages= Pages;
        this.title=Title;
        this.year=Year;
        this.available= Available;
    }

    public boolean isAvailable() {
        return available;
    }
    public int isAvailableInt(){
        return (available) ? 1:0;
    }
    public Books(int Id) {
        this.id = Id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getPages() {
        return pages;
    }

    public int getYear() {
        return year;
    }

    public String getAuthor() {
        return author;
    }
}
