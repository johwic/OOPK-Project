package javachat;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import controller.*;
import view.*;
import model.Model;

public class JavaChat extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8781964775547286356L;

	public JavaChat(String title)  {
		super(title);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				/*try {
				    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				        if ("Nimbus".equals(info.getName())) {
				            UIManager.setLookAndFeel(info.getClassName());
				            break;
				        }
				    }
				} catch (Exception e) {
					e.printStackTrace();
				}*/

				initGUI();
			}
		});
	}

	private static void initGUI() {
		JavaChat frame = new JavaChat("Java Chat");
		Model model = new Model();
		View view = new View(model);
		
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setLocationRelativeTo(null);
		
		frame.add(view);
		frame.pack();
		frame.setVisible(true);
		
		Controller controller = new Controller(model, view);
	}
}
