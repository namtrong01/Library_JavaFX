package Controller;

import Model.Books;
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
import java.util.ResourceBundle;

public class ReaderListController implements Initializable {
    Stage stage;
    Scene scene;
    Parent root;
    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField yearField;
    @FXML
    private TextField phoneField;
    @FXML
    private Text borrowingField;
    @FXML
    private Button insertButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private javafx.scene.control.TableView<Reader> TableView;
    @FXML
    private TableColumn<Reader, Integer> idColumn;
    @FXML
    private TableColumn<Reader, String> nameColumn;
    @FXML
    private TableColumn<Reader, Integer> yearColumn;
    @FXML
    private TableColumn<Reader,String> phoneColumn;
    @FXML
    private TableColumn<Reader, Integer> borrowingColumn;

    int index;

    public void backToMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/MainStage.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void insertButton() {
        if(Integer.parseInt(idField.getText())<=0 || Integer.parseInt(yearField.getText())<=1900){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(" Registation");
            alert.setHeaderText("Registation");
            alert.setContentText("Record Failed!");
            alert.showAndWait();
        }
        else{
            String query = "insert into reader values(" + idField.getText() + ",'" + nameField.getText() + "','" + yearField.getText() + "'," + phoneField.getText() + "," +0+ ")";
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
        showReader();
        idField.setText("");
        nameField.setText("");
        yearField.setText("");
        phoneField.setText("");
        borrowingField.setText("");
    }

    @FXML
    private void updateButton() {
        if(Integer.parseInt(idField.getText())<=0 || Integer.parseInt(yearField.getText())<=1900){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(" Update");

            alert.setHeaderText("Update");
            alert.setContentText("Update Failed!");
            alert.showAndWait();
        }
        else{
            String query = "update reader SET name='"+nameField.getText()+"',yearOfBirth='"+yearField.getText()+"',phone="+phoneField.getText()+",borrowing="+0+" WHERE readerID="+idField.getText()+"";
            if(executeQuery(query)) {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(" Update");

                alert.setHeaderText("Update");
                alert.setContentText("Update Successed!");
                alert.showAndWait();
            }else {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Error!");
                alert1.setHeaderText(null);
                alert1.setContentText("Failed");
                alert1.showAndWait();
            }
        }
        showReader();
        idField.setText("");
        nameField.setText("");
        yearField.setText("");
        phoneField.setText("");
        borrowingField.setText("");
    }
    @FXML
    private void deleteButton(ActionEvent event) {

        String query = "DELETE FROM reader WHERE readerID="+idField.getText()+"";
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
        showReader();
        idField.setText("");
        nameField.setText("");
        yearField.setText("");
        phoneField.setText("");
        borrowingField.setText("");
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
    public ObservableList<Reader> getReaderList(){
        ObservableList<Reader> readerList = FXCollections.observableArrayList();
        Connection connection = getConnection();
        String query = "SELECT * FROM reader";
        Statement statement;
        ResultSet resultSet;
        try{
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            Reader reader;
            while  (resultSet.next()){
                reader = new Reader(resultSet.getInt("readerID"),resultSet.getInt("yearOfBirth"),resultSet.getString("name"),resultSet.getString("phone"),resultSet.getInt("borrowing"));
                readerList.add(reader);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return readerList;
    }
    private void showReader() {
        ObservableList<Reader> list = getReaderList();

        idColumn.setCellValueFactory(new PropertyValueFactory<Reader,Integer>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Reader,String>("name"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<Reader,Integer>("yearOfBirth"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<Reader,String>("phone"));
        borrowingColumn.setCellValueFactory(new PropertyValueFactory<Reader,Integer>("borrowing"));
        TableView.setItems(list);
        TableView.setRowFactory(tv -> {
            TableRow<Reader> readerTableRow = new TableRow<>();
            readerTableRow.setOnMouseClicked(event ->
                    {
                        if (event.getClickCount() == 1 && (!readerTableRow.isEmpty())) {
                            index = TableView.getSelectionModel().getSelectedIndex();
                            idField.setText(String.valueOf(Integer.parseInt(String.valueOf(TableView.getItems().get(index).getId()))));
                            nameField.setText(TableView.getItems().get(index).getName());
                            phoneField.setText(TableView.getItems().get(index).getPhone());
                            borrowingField.setText(String.valueOf(Integer.parseInt(String.valueOf(TableView.getItems().get(index).getBorrowing()))));
                            yearField.setText(String.valueOf(Integer.parseInt(String.valueOf(TableView.getItems().get(index).getYearOfBirth()))));
                        }
                    }


            );
            return readerTableRow;

        });
        TableView.sort();


    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showReader();
    }


}
