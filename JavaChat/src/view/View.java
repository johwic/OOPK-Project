package view;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import controller.Controller;
import model.Conversation;
import model.Model;

public class View extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7946965173834172270L;
	
	private ArrayList<TabInterface> tabUI = new ArrayList<TabInterface>();
	private final Model model;
	private final JTabbedPane tabs;
	private final JPanel startPanel;
	
	private static JTextField serverPortField;
	private static JTextField serverNameField;
	private static JButton serverSubmit;
	
	private static JTextField socketIpField;
	private static JTextField socketNameField;
	private static JTextField socketPortField;
	private static JButton socketSubmit;
	
	public View(Model model) {
		this.model = model;
		
		startPanel = createStartTab();
		
		tabs = new JTabbedPane();
		tabs.add("Start", startPanel);
		
	    GroupLayout layout = new GroupLayout(this);
	    layout.setAutoCreateContainerGaps(true);
	    layout.setAutoCreateGaps(true);
	    
	    setLayout(layout);
	    layout.setHorizontalGroup(layout.createParallelGroup().addComponent(tabs));
	    layout.setVerticalGroup(layout.createParallelGroup().addComponent(tabs));
	        
//	    setLayout(new GridLayout(0, 1));
//		setBackground(Color.WHITE);
	}
	
	public void addListeners(Controller c) {
		serverPortField.getDocument().addDocumentListener(model.getDocumentListener("server_socket_port"));
		serverNameField.getDocument().addDocumentListener(model.getDocumentListener("server_socket_name"));
		serverSubmit.addActionListener(c);
		
		socketIpField.getDocument().addDocumentListener(model.getDocumentListener("socket_ip"));
		socketNameField.getDocument().addDocumentListener(model.getDocumentListener("socket_name"));
		socketPortField.getDocument().addDocumentListener(model.getDocumentListener("socket_port"));
		socketSubmit.addActionListener(c);
	}
	
	public TabInterface getTabInterface(int id) {
		return tabUI.get(id);
	}
	
	private static JPanel createStartTab() {
		
		JPanel container = new JPanel();
		container.setLayout(new GridLayout(2,2,5,5));
		
		
		JPanel startPanel1 = createServerSocketController();
        
        JPanel startPanel2 = createSocketController();
        
        JPanel startPanel3 = new JPanel();
        startPanel3.setBorder(BorderFactory.createTitledBorder(null, "Server Socket", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        
        JPanel startPanel4 = new JPanel();
        startPanel4.setBorder(BorderFactory.createTitledBorder(null, "Server Socket", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.DEFAULT_POSITION));

		container.add(startPanel1);
		container.add(startPanel2);
		container.add(startPanel3);
		container.add(startPanel4);

        return container;
	}
	
	private static JPanel createServerSocketController() {
		JPanel panel = new JPanel();
		GroupLayout layout = new GroupLayout(panel);
		
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		panel.setLayout(layout);
		panel.setBorder(BorderFactory.createTitledBorder(null, "Server Socket Controller", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        
        JLabel port = new JLabel("Port number:");
        JLabel name = new JLabel("Name:");
        
        serverPortField = new JTextField(10);
        serverNameField = new JTextField(10);
        serverSubmit = new JButton("Listen");
        
        serverSubmit.setActionCommand("server_socket_controller");
		
        layout.setHorizontalGroup(
        	layout.createSequentialGroup()
        		.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        				.addComponent(port)
        				.addComponent(name))
        		.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        				.addComponent(serverPortField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        				.addComponent(serverNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        				.addComponent(serverSubmit))
        );
        layout.setVerticalGroup(
        	layout.createSequentialGroup()
        		.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        				.addComponent(port)
        				.addComponent(serverPortField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        		.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        				.addComponent(name)
        				.addComponent(serverNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        		.addComponent(serverSubmit)
        );		
        
        
        return panel;
	}
	
	private static JPanel createSocketController() {
		JPanel panel = new JPanel();
		GroupLayout layout = new GroupLayout(panel);
		
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		panel.setLayout(layout);
		panel.setBorder(BorderFactory.createTitledBorder(null, "Connect to server", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        
		JLabel ip = new JLabel("Server IP:");
        JLabel port = new JLabel("Port number:");
        JLabel name = new JLabel("Name:");
        
        socketIpField = new JTextField(10);
        socketPortField = new JTextField(10);
        socketNameField = new JTextField(10);
        socketSubmit = new JButton("Connect");
        
        socketSubmit.setActionCommand("socket_controller"); 
		
        layout.setHorizontalGroup(
        	layout.createSequentialGroup()
        		.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        				.addComponent(ip)
        				.addComponent(port)
        				.addComponent(name))
        		.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        				.addComponent(socketIpField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        				.addComponent(socketPortField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        				.addComponent(socketNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        				.addComponent(socketSubmit))
        );
        layout.setVerticalGroup(
        	layout.createSequentialGroup()
        		.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        				.addComponent(ip)
        				.addComponent(socketIpField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))        		
        		.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        				.addComponent(port)
        				.addComponent(socketPortField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        		.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        				.addComponent(name)
        				.addComponent(socketNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        		.addComponent(socketSubmit)
        );		
        
        
        return panel;
	}	
	
	public void createTabUI(Conversation c) {
		TabInterface tab = new TabInterface(c);
		
		tabs.addTab(tab.getTitle(), tab);
		tabUI.add(tab);
	}
}
