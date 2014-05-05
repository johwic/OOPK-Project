package view;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import model.FileHandler;
import model.Message;

public class FileWindow {
	public static void createFileResponseWindow(FileHandler handler, final Message m) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				JDialog dialog = new JDialog((JFrame) null, "File transfer request", true);
				
				JPanel pane = new JPanel();
				
				JLabel message = new JLabel(m.getSender() + " wish to send you a file.");
				JLabel fileName = new JLabel("File name: " + m.getFileName());
				JLabel fileSize = new JLabel("File size: " + (double) m.getFileSize() / (double) 1024 + " KB");
				JLabel fileMessage = new JLabel("Message: " + m.getFileMessage());
				
				JLabel portLabel = new JLabel("Port: ");
				JLabel messageLabel = new JLabel("Message: ");
				
				JTextField portField = new JTextField(10);
				JTextField messageField = new JTextField(10);
				
				JButton accept = new JButton("Accept");
				JButton decline = new JButton("Decline");
				
				pane.add(message);
				pane.add(fileName);
				pane.add(fileSize);
				pane.add(fileMessage);
				
				dialog.add(pane);
				dialog.setVisible(true);
			}
		});
	}
}
