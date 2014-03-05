package javachat;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import controller.*;
import view.*;
import model.Model;

public class JavaChat {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
				    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                        // Nimbus theme
				        if ("Nimbus".equals(info.getName())) {
				            UIManager.setLookAndFeel(info.getClassName());
				            break;
				        }
				    }
				} catch (Exception e) {
					e.printStackTrace();
				}

				initGUI();
			}
		});
	}

	private static void initGUI() {

        // Main window

		JFrame frame = new JFrame("Java Chat");

        // MVC
		Model model = new Model();
		View view = new View(model);
		@SuppressWarnings("unused")
		Controller controller = new Controller(model, view);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(800, 600));
		frame.setMinimumSize(new Dimension(800, 600));
		frame.setLocationRelativeTo(null);
		
		frame.add(view);
		frame.pack();
		frame.setVisible(true);
	}
}
