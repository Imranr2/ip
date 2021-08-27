package lifeline;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * The Main class starts the program
 */
public class Main extends Application {

    /**
     * Starts the GUI program
     *
     * @param stage Main stage which represents primary window of JavaFX application
     */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            scene.getStylesheets().add("styles/lifeline.css");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts the console program
     *
     * @param args Does not accept any arguments
     */
    public static void main(String[] args) {
        Lifeline lifeline = new Lifeline("save" + File.separator + "tasks.json");
        lifeline.start();
    }
}
