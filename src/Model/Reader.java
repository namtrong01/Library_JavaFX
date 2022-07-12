package Model;

public class Reader {
    private int id;
    private int yearOfBirth;
    private String name;
    private String phone;
    private int borrowing;

    public Reader(int id, int yearOfBirth, String name, String phone, int borrowing) {
        this.id = id;
        this.yearOfBirth = yearOfBirth;
        this.name = name;
        this.phone = phone;
        this.borrowing = borrowing;
    }



    public int getId() {
        return id;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public int getBorrowing() {
        return borrowing;
    }
}
