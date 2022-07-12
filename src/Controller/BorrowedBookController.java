package Controller;

import Model.Books;
import Model.BorrowingBook;
import Model.Reader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BorrowedBookController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    int index;
    @FXML
    TableView<BorrowingBook> TableView;
    @FXML
    TableColumn<BorrowingBook, Integer> bookIdColumn;
    @FXML
    TableColumn<BorrowingBook, Integer> readerIdColumn;
    @FXML
    TableColumn<BorrowingBook, Date> issueDateColumn;
    @FXML
    TableColumn<BorrowingBook, Date> dueDateColumn;


    @FXML
    private TextField bookIdInput;
    @FXML
    private Text title;
    @FXML
    private Text author;
    @FXML
    private Text bookYear;
    @FXML
    private Text pages;
    @FXML
    private TextField readerIdInput;
    @FXML
    private Text name;
    @FXML
    private Text phone;
    @FXML
    private Text yearOfBirth;
    @FXML
    private Text borrowing;
    @FXML
    private Text isAvailable;

    public void backToMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/MainStage.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public Connection getConnection() {
        Connection conn;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/library","root","1234");
            return conn;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    private boolean  executeQuery(String query) {
        Connection conn = getConnection();
        Statement st;
        try {
            st = conn.createStatement();
            st.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error:" + e.getMessage(), "Error Occurred", JOptionPane.ERROR_MESSAGE);
            System.out.println(e.getLocalizedMessage());
            return false;
        }
    }
    @FXML
    void showBookInfo(ActionEvent event) throws SQLException {
        String id = bookIdInput.getText();
        String query = "SELECT * FROM books WHERE bookID = '" + id + "'";
        Connection connection = getConnection();
        Statement st =connection.createStatement();
        ResultSet rs = st.executeQuery(query);
        Boolean flag = false;

        try {
            while (rs.next()) {
                String bTitle = rs.getString("title");
                String bAuthor = rs.getString("author");
                Boolean bAvail = rs.getBoolean("available");
                Integer bYear = rs.getInt("year");
                Integer bPages = rs.getInt("pages");
                title.setText(bTitle);
                author.setText(bAuthor);
                bookYear.setText(String.valueOf(bYear));
                pages.setText(String.valueOf(bPages));
                String avail = (bAvail)?"Available" : "Not Available";
                isAvailable.setText(avail);
                flag = true;
               }
               if (!flag) {
                   title.setText("There is no Book in this ID!");
                   author.setText("---");
                   isAvailable.setText("Unavailable");
                   pages.setText("---");
                   bookYear.setText("---");
               }
           } catch (SQLException ex) {
              // Logger.getLogger(BorrowedBookController.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
           }
       }
    @FXML
    void showReaderInfo(ActionEvent event) throws SQLException {
        String id = readerIdInput.getText();
        String query = "SELECT * FROM reader WHERE readerID = '" + id + "'";
        Connection connection = getConnection();
        Statement st =connection.createStatement();
        ResultSet rs = st.executeQuery(query);
        Boolean flag = false;

        try {
            while (rs.next()) {
                String rName = rs.getString("name");
                String rPhone = rs.getString("phone");
                String rBorrowing = rs.getString("borrowing");
                Integer rYear = rs.getInt("yearOfBirth");

                name.setText(rName);
                phone.setText(rPhone);
                yearOfBirth.setText(String.valueOf(rYear));
                borrowing.setText(rBorrowing);


                flag = true;
            }
            if (!flag) {
                name.setText("There is no Reader in this ID!");
                phone.setText("---");

                yearOfBirth.setText("---");
                borrowing.setText("---");
            }
        } catch (SQLException ex) {
            // Logger.getLogger(BorrowedBookController.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }



    @FXML
    public void doOperation(ActionEvent event){
        String bookID = bookIdInput.getText();
        String readerID = readerIdInput.getText();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Issue Operation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure want to issue the book " + title.getText() + "\n to " + name.getText() + "?");
        Optional<ButtonType> response = alert.showAndWait();
        if (response.get() == ButtonType.OK) {
            String str = "INSERT INTO borrowing(bookID, readerID) VALUES ("
                    + "'" + bookID + "',"
                    + "'" + readerID + "')";
            String str2 = "UPDATE BOOKs SET available = false WHERE bookID = '" + bookID + "'";
            String str3 = "Update reader set borrowing =borrowing+1 where readerID=  '"+readerID+"'";
            if (executeQuery(str) && executeQuery(str2)&&executeQuery(str3)) {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("");
                alert1.setHeaderText(null);
                alert1.setContentText("Successful");
                alert1.showAndWait();
                bookIdInput.setText("");
                title.setText("");
                author.setText("");
                bookYear.setText("");
                pages.setText("");
                readerIdInput.setText("");
                name.setText("");
                phone.setText("");
                yearOfBirth.setText("");
                borrowing.setText("");
                isAvailable.setText("");
            } else {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Error!");
                alert1.setHeaderText(null);
                alert1.setContentText("Failed");
                alert1.showAndWait();
            }
        }
        else {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setTitle("");
            alert1.setHeaderText(null);
            alert1.setContentText("Canceled");
            alert1.showAndWait();
        }
        showTableBorrowing();
    }
    @FXML
    public void returnBook(ActionEvent e) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm return Operation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure want to return the book?");
        Optional<ButtonType> response = alert.showAndWait();
        if (response.get() == ButtonType.OK) {
            String bookID = bookIdInput.getText();
            String query1 = "delete from borrowing where bookID='" + bId + "'";
            String query2 = "update books set available= true where bookID='" + bId + "'";
            String query3 = "update reader set borrowing =borrowing-1 where readerID='" + rId + "'";
            if (executeQuery(query1)&&executeQuery(query3)&&executeQuery(query2)) {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Successful");
                alert1.setHeaderText(null);
                alert1.setContentText("Book Has Been Returned");
                alert1.showAndWait();
            } else {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Failed");
                alert1.setHeaderText(null);
                alert1.setContentText("Failed");
                alert1.showAndWait();
            }
        }else {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setTitle("");
            alert1.setHeaderText(null);
            alert1.setContentText("Canceled");
            alert1.showAndWait();
        }

        title.setText("");
        author.setText("");
        bookYear.setText("");
        pages.setText("");

        name.setText("");
        phone.setText("");
        yearOfBirth.setText("");
        borrowing.setText("");
        isAvailable.setText("");
        showTableBorrowing();
    }
    public ObservableList<BorrowingBook> getBorrowingList(){
        ObservableList<BorrowingBook> borrowingList = FXCollections.observableArrayList();
        Connection connection = getConnection();
        String query = "select * from borrowing";
        Statement statement;
        ResultSet resultSet;
        try{
            statement =connection.createStatement();
            resultSet =statement.executeQuery(query);
            BorrowingBook borrowingBook;
            while(resultSet.next()){
                borrowingBook= new BorrowingBook(resultSet.getInt("bookID"),resultSet.getInt("readerID"),resultSet.getDate("issueDate"),resultSet.getDate("dueDate"));
                borrowingList.add(borrowingBook);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return borrowingList;
    }
    String bId;
    String rId;
    public void showTableBorrowing(){
        ObservableList<BorrowingBook> list = getBorrowingList();
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<BorrowingBook,Integer>("bookID"));
        readerIdColumn.setCellValueFactory(new PropertyValueFactory<BorrowingBook,Integer>("readerID"));
        issueDateColumn.setCellValueFactory(new PropertyValueFactory<BorrowingBook,Date>("issueDate"));
        dueDateColumn.setCellValueFactory(new  PropertyValueFactory<BorrowingBook,Date>("dueDate"));
        TableView.setItems(list);
        TableView.setRowFactory(tv->{
            TableRow<BorrowingBook> tableRow= new TableRow<>();
            tableRow.setOnMouseClicked(mouseEvent ->
            {
                if(mouseEvent.getClickCount()==1&&(!tableRow.isEmpty())){
                    index=TableView.getSelectionModel().getSelectedIndex();
                    bookIdInput.setText(String.valueOf(Integer.parseInt(String.valueOf(TableView.getItems().get(index).getBookID()))));
                    readerIdInput.setText(String.valueOf(Integer.parseInt(String.valueOf(TableView.getItems().get(index).getReaderID()))));
                    bId = String.valueOf(Integer.parseInt(String.valueOf(TableView.getItems().get(index).getBookID())));
                    rId = String.valueOf(Integer.parseInt(String.valueOf(TableView.getItems().get(index).getReaderID())));

                }
            });
            return tableRow;
        });
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showTableBorrowing();
    }
}
