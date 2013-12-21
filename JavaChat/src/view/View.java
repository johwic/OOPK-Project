package view;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import controller.Controller;
import model.Model;

public class View extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7946965173834172270L;
	
	private TabInterface tabUI;
	private final Model model;
	
	public View(Model model) {
		this.model = model;
		JTabbedPane tabs = new JTabbedPane();
		
		add(tabs);

		tabUI = new TabInterface();
		
		tabs.addTab("Chat window 1", tabUI);
		tabs.setToolTipText("Chatting with Johan");
		
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setBackground(Color.WHITE);
		
		tabUI.getMessagePane().setText(model.getText());
	}
	
	public void addListeners(Controller c) {
	}
	
	public TabInterface getTabInterface() {
		return tabUI;
	}
}
