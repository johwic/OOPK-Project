package view;

import java.awt.Dimension;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import controller.Controller;

public class TabInterface extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3884551684205295307L;
	
	private final JTextArea messageInput;
	private final JButton submit;
	private final JButton reset;
	private final JTextPane messageWindow;
	
	public TabInterface() {
		messageInput = new JTextArea();
		messageWindow = new JTextPane();
		submit = new JButton("Submit");
		reset = new JButton("Reset");
		
		JLabel label = new JLabel("Message:");
		JScrollPane jScrollPane1 = new JScrollPane(messageInput);
		
		messageInput.setLineWrap(true);
		messageInput.setWrapStyleWord(true);
		messageInput.setColumns(24);
		messageInput.setRows(5);
		
		JScrollPane window = new JScrollPane(messageWindow);
		
		messageWindow.setPreferredSize(new Dimension(400, 300));
		messageWindow.setEditable(false);
		messageWindow.setContentType("text/html; charset= UTF-8");
		
		//messageWindow.setText("<b>Johan: </b>Hejbhh hhhhhhhhhhhhhh hhhhhhh hhhhhhhhhhh hhhhhhhhhhhh hhhhhhhhhhhhhhhhh hhhhhhhhhh hhhhhhhhhhhhh hhhhhhhhhh hhhhhh hhhhhhhhhh <font color=\"yellow\">då!</font>");
		
		//window.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		//jScrollPane1.setPreferredSize(new Dimension(256, 256));
		
        GroupLayout layout = new GroupLayout(this);
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        
        setLayout(layout);
        layout.setHorizontalGroup(
        	layout.createParallelGroup()
        	.addComponent(window)
        	.addGroup(
            layout.createSequentialGroup()
                .addComponent(label)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                	.addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(submit)
                        .addComponent(reset))))
        );
        
        layout.setVerticalGroup(
            	layout.createSequentialGroup()
            	.addComponent(window)
            	.addGroup(
        		layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(label)
                    .addComponent(jScrollPane1))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(submit)
                    .addComponent(reset)))
        );
	}
	
	public void addListeners(Controller c) {
		
	}
	
	public void append(String s) throws BadLocationException, IOException {
		HTMLDocument doc = (HTMLDocument) messageWindow.getDocument();
		HTMLEditorKit kit = (HTMLEditorKit) messageWindow.getEditorKit();
		//doc.insertString(doc.getLength(), s, null);
		kit.insertHTML(doc, doc.getLength(), s, 0, 0, null);
	}
	
	public JTextPane getMessagePane() {
		return messageWindow;
	}
}
