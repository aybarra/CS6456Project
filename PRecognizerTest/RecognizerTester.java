import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;
import java.util.ArrayList;

public class RecognizerTester extends JPanel {

	public RecognizerTester() {
		super(new BorderLayout());

		Recognizer recognizer = new Recognizer();

		GesturePanel gesturePanel = new GesturePanel();
		add(gesturePanel, BorderLayout.CENTER);

		JLabel detectedGestureLabel = new JLabel("Detected Gesture:");
		JLabel currentDetectedGestureLabel = new JLabel("None");
		detectedGestureLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		currentDetectedGestureLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
		detectedGestureLabel.setBorder(new EmptyBorder(0, 0, 0, 10));

		JPanel labelPanel = new JPanel();
		labelPanel.add(detectedGestureLabel);
		labelPanel.add(currentDetectedGestureLabel);

		JButton recognizeButton = new JButton("Recognize");
		recognizeButton.addActionListener(e -> {
			ArrayList<Point> points = gesturePanel.getPoints();
			ArrayList<RecognizerResult> result = recognizer.recognize(points);
			gesturePanel.clear();
			if (result.isEmpty()) {
				currentDetectedGestureLabel.setText("Failed to recognize");
			} else if (result.size() == 1) {
				RecognizerResult rr = result.get(0);
				currentDetectedGestureLabel.setText(rr.gesture.name + String.format(" (Score: %.3f)", rr.score));
			} else {
				currentDetectedGestureLabel.setText(result.toString());
			}
		});

		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.add(labelPanel, BorderLayout.CENTER);
		bottomPanel.add(recognizeButton, BorderLayout.EAST);
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
		add(bottomPanel, BorderLayout.SOUTH);
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
