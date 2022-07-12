package Model;

import java.util.Date;

public class BorrowingBook {

    private int readerID;
    private int bookID;
    private Date issueDate;
    private Date dueDate;

    public BorrowingBook( int bookID, int readerID, Date issueDate, Date dueDate) {

        this.readerID = readerID;
        this.bookID = bookID;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
    }



    public int getReaderID() {
        return readerID;
    }

    public int getBookID() {
        return bookID;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public Date getDueDate() {
        return dueDate;
    }
}
