import javax.swing.*;

public class GUI {

    public static void initializeGUI() {
        System.out.println("Starting GUI...");
        JFrame frame = new JFrame("Java IDE");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JLabel label = new JLabel("This is a Java IDE");
        frame.getContentPane().add(label);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                initializeGUI();
            }
        });
    }
}