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
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.scene.control.ScrollPane;

import java.io.IOException;
import java.io.*;
import java.lang.*;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.*;
import java.util.stream.Stream;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.*;

import org.apache.commons.lang3.StringUtils;

public class GUI extends Application {
    static File filename;
    static File gitConfigFile;
    Stage primaryStage;

    StringBuilder sb = new StringBuilder();
    StringBuilder sb1 = new StringBuilder();
    StringBuilder sb2 = new StringBuilder();
    static int count = 0;
    static String line;
    int a = 1;
    int b = 0;

    // static Git git;

    @FXML
    public javafx.scene.text.Text numberText;
    @FXML
    public ScrollPane scroll;
    @FXML
    private Button run;
    @FXML
    public TextArea output;
    @FXML
    public TextArea textedit;
    @FXML
    public TextField workingDirectory;
    @FXML
    public TextField clonePath;
    @FXML
    public TextField addFile;
    @FXML
    public TextField commitMessage;
    @FXML
    public TextField gitToken;
    // @FXML
    // public TextArea gitText;

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
                    if (numberText.getText().equals("1")) {
                        sb1.append("1  ");
                    }
                    a = a + 1;
                    int i = 0;

                    int len1 = 3 - String.valueOf(a).length();
                    sb1.append(String.valueOf(a));
                    while (i < len1) {
                        sb1.append(" ");
                        ;
                        i++;
                    }
                    numberText.setText(sb1.toString());
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
    public void save2(KeyEvent keyEvent) {
        try {
            System.out.println("save the changes");
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
            // output.appendText("\n");
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
        // System.out.println("GOOD: '" + filename.getName().substring(0,
        // filename.getName().length() - 5) + "'");
        // String runString = StringUtils.substringBetween(filename.getName(), "(",
        // ")");
        String command = "java -cp " + filename.getParent() + " "
                + filename.getName().substring(0, filename.getName().length() - 5);
        System.out.println(command);
        // System.out.println("Working Directory = " + System.getProperty("user.dir"));
        Process pro = null;

        try {
            pro = Runtime.getRuntime().exec(command);
            printLines(command + " stdout:", pro.getInputStream());
            printLines(command + " stderr:", pro.getErrorStream());
            // output.appendText("\n");
            pro.waitFor();
            String line2 = command + " exitValue() " + pro.exitValue() + "\n";
            output.appendText(line2);
            System.out.println(command + " exitValue() " + pro.exitValue());
        } catch (IOException | InterruptedException ioException) {
            ioException.printStackTrace();
        }
    }

    @FXML
    public void enter(KeyEvent keyEvent) {
        if (numberText.getText().equals("1")) {

            sb1.append("1  ");
        }

        switch (keyEvent.getCode()) {
            case ENTER:
                a = a + 1;
                int i = 0;

                int len1 = 3 - String.valueOf(a).length();
                sb1.append(String.valueOf(a));
                while (i < len1) {
                    sb1.append(" ");
                    i++;
                }
                numberText.setText(sb1.toString());
                scroll.setVvalue(1.0);
                scroll.setPannable(true);

                scroll.viewportBoundsProperty();
        }
    }

    @FXML
    public void format(ActionEvent actionEvent) {
        System.out.println("Invoked");
        StringBuilder sb = new StringBuilder();
        line = " ";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                if (!((line = br.readLine()) != null))
                    break;
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (line.contains("package") | (line.contains("import"))) {
                if (line.contains(";"))
                    sb.append(line.replace(";", ";\n").stripLeading());
                else {
                    sb.append(line.stripLeading());
                }
            }

            if (line.contains(";") && !line.contains("for")) {
                if (!(line.contains("package") | (line.contains("import")))) {
                    if (line.stripLeading().length() <= 1) {
                        count = count - 4;
                        sb.append(line.replace(";", ";\n").stripLeading());
                    } else {
                        if (!line.contains("}")) {
                            int i = 0;
                            while (i < count) {
                                sb.append(" ");
                                i++;
                            }
                            sb.append(line.replace(";", ";\n").stripLeading());
                        }
                    }
                }
            }
            if (line.contains("{")) {
                if (line.stripLeading().length() <= 1) {
                    sb.append(line.replace("{", "{\n").stripLeading());
                } else {
                    int i = 0;
                    while (i < count) {
                        sb.append(" ");
                        i++;

                    }
                    sb.append(line.replace("{", "{\n").stripLeading());
                    count = count + 4;
                }
            }
            if (line.contains("}")) {
                int i = 4;
                while (i < count) {
                    sb.append(" ");
                    i++;
                }
                sb.append(line.replace("}", "}\n").stripLeading());
                count = count - 4;
            }
            if (!(line.contains("{") | line.contains(";") | line.contains("}") | line.length() == 0)) {
                int i = 0;
                while (i < count) {
                    sb.append(" ");
                    i++;
                }
                sb.append(line.stripLeading());
                count = count + 4;
            }
            if (!(line.contains("{")) && line.contains("for")) {
                count = count + 4;
                int i = 0;
                while (i < count) {
                    sb.append(" ");
                    i++;
                }
                sb.append(line.stripLeading());
                sb.append("\n");
                // count = count + 1;
            }
        }
        textedit.setText(sb.toString());
        System.out.println(sb.toString());
    }

    @FXML
    public void configure(ActionEvent actionEvent) {
        // System.out.println("Configure Git");
        // try {
        //     PrintWriter writer;
        //     writer = new PrintWriter("gitconfig.txt");
        //     writer.println(gitText.getText());
        //     writer.close();
        // } catch (IOException ie) {
        //     System.out.print(ie);
        // }
        // output.appendText("Git Config File Set");
        // System.out.println(filename);
        // gitConfigFile = filename;
    }

    @FXML
    public void clone(ActionEvent actionEvent) {
        System.out.println("Clone");
        //wd is the directory we want to clone to
        //cp is the http address that we want to clone from ex: https:
        String wd = workingDirectory.getText();
        String cp = clonePath.getText();
        System.out.println(wd);

        Path localPath = Paths.get(wd);
        try {
            Git git = Git.cloneRepository().setURI(cp).setDirectory(localPath.toFile()).call();
        } catch(GitAPIException e) {

        }
    }

    @FXML
    public void add(ActionEvent actionEvent) {
        System.out.println("Add");
        //wd is the current directory that we want add to
        //af is the file we want to add
        String af = addFile.getText();
        String wd = workingDirectory.getText();
        System.out.println(af);

        Path localPath = Paths.get(wd);
        try {
            Git git = Git.open(localPath.toFile());
            git.add().addFilepattern(af).call();
        } catch (GitAPIException | IOException e) {

        }
        
    }

    @FXML
    public void commit(ActionEvent actionEvent) {
        System.out.println("Commit");
        //wd is the current directory that we want commit to
        //cm is the message to send with the commit
        String cm = commitMessage.getText();
        String wd = workingDirectory.getText();
        System.out.println(cm);

        Path localPath = Paths.get(wd);
        try {
            Git git = Git.open(localPath.toFile());
            git.commit().setMessage(cm).call();
        } catch (GitAPIException | IOException e) {

        }
        //git.commit().setMessage("create file2").setAuthor("author", "author@email.com").call();
    }

    @FXML
    public void push(ActionEvent actionEvent) {
        System.out.println("Push");
        String wd = workingDirectory.getText();
        String cp = clonePath.getText();
        String gt = gitToken.getText();
        //example push url: "https://<GITHUB_ACCESS_TOKEN>@github.com/<GITHUB_USERNAME>/<REPOSITORY_NAME>.git"
        //example: https://<GITHUB_ACCESS_TOKEN>@github.com/bernardtran99/ideFilesTest.git;
        //cp is the url of the repository to be pushed to, then we add gt into cp in order to generate the complete push url
        //Assumption: a git token is needed to push to a remote reposity because we use http and not ssh (github token)
        // System.out.println(gt);

        Path localPath = Paths.get(wd);
        // String[] sarray = cp.split("://",2);
        // System.out.println(sarray[0]);
        // System.out.println(sarray[1]);
        // String completePushStr = sarray[0] + "://" + gt + "@" + sarray[1];
        // System.out.println(completePushStr);
        try {
            Git git = Git.open(localPath.toFile());
            CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(gt, "");
            git.push().setRemote(cp).setCredentialsProvider(credentialsProvider).call();
            output.appendText(git.toString());
        } catch (GitAPIException | IOException e) {

        }
        // File repoPath = gitConfigFile;
        // //String remoteUrl = "https://<GITHUB_ACCESS_TOKEN>@github.com/<GITHUB_USERNAME>/<REPOSITORY_NAME>.git";
        // String remoteUrl = "https://${token}@github.com/user/repo.git";
        // try {
        //     git = Git.init().setDirectory(repoPath).call();
        //     CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider("${token}", "");
        //     git.push().setRemote(remoteUrl).setCredentialsProvider(credentialsProvider).call();
        //     // PushCommand pushCommand = git.push();
        //     // pushCommand.add("master");
        //     // pushCommand.setRemote("origin");
        //     // pushCommand.call();
        // } catch(GitAPIException e) {
            
        // }
    }

    public void printStatus() {
        
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
