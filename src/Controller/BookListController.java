package Controller;

import Model.Books;
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
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class BookListController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TextField idField;
    @FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField yearField;
    @FXML
    private TextField pagesField;
    @FXML
    private Button insertButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private javafx.scene.control.TableView<Books> TableView;
    @FXML
    private TableColumn<Books, Integer> idColumn;
    @FXML
    private TableColumn<Books, String> titleColumn;
    @FXML
    private TableColumn<Books, String> authorColumn;
    @FXML
    private TableColumn<Books,Integer> yearColumn;
    @FXML
    private TableColumn<Books, Integer> pagesColumn;
    @FXML
    private TableColumn<Books, Boolean> availableColumn;
    @FXML
    private CheckBox aCheckBox;


    int index;

    public void backToMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/MainStage.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void insertButton() {
        if(Integer.parseInt(idField.getText())<=0 || Integer.parseInt(pagesField.getText())<=0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(" Registation");

            alert.setHeaderText("Registation");
            alert.setContentText("Record Failed!");
            alert.showAndWait();
        }
        else{
            String query = "insert into books values(" + idField.getText() + ",'" + titleField.getText() + "','" + authorField.getText() + "'," + yearField.getText() + "," + pagesField.getText() + ",true ";
            if(executeQuery(query)){

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(" Registation");

                alert.setHeaderText("Registation");
                alert.setContentText("Record Addedddd!");
                alert.showAndWait();
            }else {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Error!");
                alert1.setHeaderText(null);
                alert1.setContentText("Failed");
                alert1.showAndWait();
            }
        }
        showBooks();
        idField.setText("");
        pagesField.setText("");
        yearField.setText("");
        titleField.setText("");
        authorField.setText("");
        aCheckBox.setSelected(true);
    }

    @FXML
    private void updateButton() {
        if(Integer.parseInt(idField.getText())<=0 || Integer.parseInt(pagesField.getText())<=0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(" Update");

            alert.setHeaderText("Update");
            alert.setContentText("Update Failed!");
            alert.showAndWait();
        }
        else{
            String query = "update books SET title='"+titleField.getText()+"',author='"+authorField.getText()+"',year='"+yearField.getText()+"',pages='"+pagesField.getText()+"'     WHERE bookID="+idField.getText()+"";
            if(executeQuery(query)) {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(" Update");

                alert.setHeaderText("Update");
                alert.setContentText("Update Successed!");
                alert.showAndWait();
            } else {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Error!");
                alert1.setHeaderText(null);
                alert1.setContentText("Failed");
                alert1.showAndWait();
            }
        }
        showBooks();
        idField.setText("");
        pagesField.setText("");
        yearField.setText("");
        titleField.setText("");
        authorField.setText("");
        aCheckBox.setSelected(true);

    }
    @FXML
    private void deleteButton(ActionEvent event) {

        String query = "DELETE FROM books WHERE bookID="+idField.getText()+"";
        if(executeQuery(query)) {
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("");
            alert1.setHeaderText(null);
            alert1.setContentText("Successful");
            alert1.showAndWait();
            showBooks();
            idField.setText("");
            pagesField.setText("");
            yearField.setText("");
            titleField.setText("");
            authorField.setText("");
            aCheckBox.setSelected(true);
        }else {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setTitle("Error!");
            alert1.setHeaderText(null);
            alert1.setContentText("Failed");
            alert1.showAndWait();
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
    public ObservableList<Books> getBooksList(){
        ObservableList<Books> booksList = FXCollections.observableArrayList();
        Connection connection = getConnection();
        String query = "SELECT * FROM books";
        Statement statement;
        ResultSet resultSet;
        try{
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            Books books;
            while  (resultSet.next()){
                Boolean bAvail = resultSet.getBoolean("available");
                String avail= (bAvail)?"Available" : "Not Available";
                books = new Books(resultSet.getInt("bookID"),resultSet.getString("title"),resultSet.getString("author"),resultSet.getInt("year"),resultSet.getInt("pages"),resultSet.getBoolean("available"));
                booksList.add(books);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return booksList;
    }
    private void showBooks() {
        ObservableList<Books> list = getBooksList();

        idColumn.setCellValueFactory(new PropertyValueFactory<Books,Integer>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<Books,String>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<Books,String>("author"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<Books,Integer>("year"));
        pagesColumn.setCellValueFactory(new PropertyValueFactory<Books,Integer>("pages"));
        availableColumn.setCellValueFactory(new PropertyValueFactory<Books,Boolean>("available"));
        TableView.setItems(list);
        TableView.setRowFactory(tv -> {
            TableRow<Books> booksTableRow = new TableRow<>();
            booksTableRow.setOnMouseClicked(event ->
                    {
                        if (event.getClickCount() == 1 && (!booksTableRow.isEmpty())) {
                            index = TableView.getSelectionModel().getSelectedIndex();
                            idField.setText(String.valueOf(Integer.parseInt(String.valueOf(TableView.getItems().get(index).getId()))));
                            authorField.setText(TableView.getItems().get(index).getAuthor());
                            titleField.setText(TableView.getItems().get(index).getTitle());
                            pagesField.setText(String.valueOf(Integer.parseInt(String.valueOf(TableView.getItems().get(index).getPages()))));
                            yearField.setText(String.valueOf(Integer.parseInt(String.valueOf(TableView.getItems().get(index).getYear()))));
                            aCheckBox.setSelected(TableView.getItems().get(index).isAvailable());

                        }
                    }


            );
            return booksTableRow;

        });
        TableView.sort();


    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showBooks();
    }


}
