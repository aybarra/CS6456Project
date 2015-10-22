import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;

public class RecognizerTester extends JPanel {

	public RecognizerTester() {
		super(new BorderLayout());

		GesturePanel gesturePanel = new GesturePanel();
		add(gesturePanel, BorderLayout.CENTER);

		JLabel detectedGestureLabel = new JLabel("Detected Gesture:");
		JLabel currentDetectedGestureLabel = new JLabel("None");
		detectedGestureLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		currentDetectedGestureLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		detectedGestureLabel.setBorder(new EmptyBorder(0, 0, 0, 20));

		JPanel labelPanel = new JPanel();
		labelPanel.add(detectedGestureLabel);
		labelPanel.add(currentDetectedGestureLabel);
		labelPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		add(labelPanel, BorderLayout.SOUTH);
	}

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Recognizer Tester");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(new RecognizerTester(), BorderLayout.CENTER);
        frame.setPreferredSize(new Dimension(750, 450));
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }

	public static void main(String[] args) {
        SwingUtilities.invokeLater(RecognizerTester::createAndShowGUI);
    }
}
