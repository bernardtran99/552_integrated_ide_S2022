import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Calculator extends Application {
  int currentNumber;
  int previousNumber;
  Operation operation;

  @FXML
  private Text numberDisplayText;
  @FXML
  private Text operatorText;

  @Override
  public void start(Stage stage) throws IOException {
    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("calculator.fxml")));

    Scene scene = new Scene(root, 300, 275);

    stage.setTitle("FXML Calculator");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }

  /** This method will run when a number button is clicked because of the line onAction="#clickNumber"
   *  in the FXML file.
   *
   * @param actionEvent the click event
   */
  @FXML
  public void clickNumber(javafx.event.ActionEvent actionEvent) {
    // TODO you can tell what number was clicked by using the text of the clicked button.  The clicked button is considered the target of the action
    // TODO how can you construct a longer number from its individual digits
    // TODO don't forget to update the displayed number
  }

  @FXML
  public void clickOperator(javafx.event.ActionEvent actionEvent) {
    clickEquals();
    // when you click an operator it should evaluate the previous expression, which is handled by the above line.
    // TODO determine what operator was clicked using Operator.fromSymbol
    // TODO when an operator is clicked, you have to move the currently displayed number to the previous number register
  }

  public void clearDisplay() {
    // TODO implement this method
  }

  public void clickEquals() {
    // TODO implement this method so that, if an operator was pressed, it calls the apply method on the operator with the last two numbers typed in
  }
}