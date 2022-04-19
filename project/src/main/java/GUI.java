import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;

import java.io.IOException;
import java.io.*;
import java.lang.*;
import java.util.*;
import java.nio.file.Files;
import java.util.stream.Stream;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;

import org.apache.commons.lang3.StringUtils;


public class GUI extends Application {
    static File filename;
    Stage primaryStage;
    @FXML
    private Button run;
    @FXML
    public TextArea output;
    @FXML
    public TextArea textedit;

    public static void main(String[] args) {
        launch();
    }
    
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("contents.fxml")));

        Scene scene = new Scene(root, 300, 275);

        stage.setTitle("Java Integrated Development Environment");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public static void filelocation(File file) {
        filename = file;
    }

    @FXML
    public void openAction(ActionEvent actionEvent) {
        FileChooser file = new FileChooser();
        file.setTitle("Open File");
        File fileToLoad = file.showOpenDialog(primaryStage);
        filelocation(fileToLoad);

        System.out.println(file.getTitle());
        System.out.println(fileToLoad);

        if (fileToLoad != null) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(fileToLoad));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // Use Files.lines() to calculate total lines - used for progress
            long lineCount;
            try (Stream<String> stream = Files.lines(fileToLoad.toPath())) {
                lineCount = stream.count();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Load in all lines one by one into a StringBuilder separated by "\n" -
            // compatible with TextArea
            String line;
            StringBuilder totalFile = new StringBuilder();

            while (true) {
                try {
                    if (!((line = reader.readLine()) != null))
                        break;
                    textedit.appendText(line);
                    textedit.appendText("\n");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            filename = fileToLoad;
            System.out.println(totalFile);
        }
    }

    @FXML
    public void saveAs(ActionEvent actionEvent) {
        FileChooser file = new FileChooser();
        file.setTitle("Save Image");
        File file1 = file.showSaveDialog(primaryStage);
        filelocation(file1);
        try {
            PrintWriter writer;
            writer = new PrintWriter(file1);
            writer.println(textedit.getText());
            writer.close();
        } catch (IOException ie) {
            System.out.print(ie);
        }
        System.out.println(file1);
        filename = file1;
    }

    @FXML
    public void save(ActionEvent actionEvent) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(filename);
            writer.println(textedit.getText());
            writer.close();
        } catch (IOException ie) {
            System.out.print(ie);
        }
        System.out.println(filename);
    }

    @FXML
    public void compile(ActionEvent actionEvent) {
        System.out.println(filename);
        String command = "javac " + filename;
        System.out.println(command);
        Process pro = null;

        try {
            pro = Runtime.getRuntime().exec(command);
            printLines(command + " stdout:", pro.getInputStream());
            printLines(command + " stderr:", pro.getErrorStream());
            //output.appendText("\n");
            pro.waitFor();
            String line2 = command + " exitValue() " + pro.exitValue() + "\n";
            output.appendText(line2);
            System.out.println(command + " exitValue() " + pro.exitValue() + "\n");
        } catch (IOException | InterruptedException ioException) {
            ioException.printStackTrace();
        }
    }

    @FXML
    public void run(ActionEvent actionEvent) {
        System.out.println(filename);
        // System.out.println("FILENAME: " + filename.getAbsolutePath());
        // System.out.println("FILENAME: " + filename.getName());
        // System.out.println("FILENAME: " + filename.getPath());
        // System.out.println("FILENAME: " + filename.getParent());
        // System.out.println("GOOD: '" + filename.getName().substring(0, filename.getName().length() - 5) + "'");
        //String runString = StringUtils.substringBetween(filename.getName(), "(", ")");
        String command = "java -cp " + filename.getParent() + " " + filename.getName().substring(0, filename.getName().length() - 5);
        System.out.println(command);
        // System.out.println("Working Directory = " + System.getProperty("user.dir"));
        Process pro = null;

        try {
            pro = Runtime.getRuntime().exec(command);
            printLines(command + " stdout:", pro.getInputStream());
            printLines(command + " stderr:", pro.getErrorStream());
            //output.appendText("\n");
            pro.waitFor();
            String line2 = command + " exitValue() " + pro.exitValue() + "\n";
            output.appendText(line2);
            System.out.println(command + " exitValue() " + pro.exitValue());
        } catch (IOException | InterruptedException ioException) {
            ioException.printStackTrace();
        }
    }

    public void printLines(String cmd, InputStream ins) {
        String line;
        BufferedReader in = new BufferedReader(new InputStreamReader(ins));
        while (true) {
            try {
                line = in.readLine();
                System.out.println(line);
                if (line == null) {
                    break;
                }
                output.appendText(cmd + " " + line + "\n");

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
