package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import model.Conversation;
import model.SocketThread;

public class TabInterface extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3884551684205295307L;

	private static int count = 0;

	private final JTextArea messageInput;
	private final JButton submit;
	private final JButton reset;
	private final JTextPane messageWindow;
	private final Conversation conversation;
	private final int id;
	private final JColorChooser chooser;

	public TabInterface(Conversation c) {
		id = count;
		count++;

		conversation = c;

		messageInput = new JTextArea();
		messageWindow = new JTextPane();
		submit = new JButton("Submit");
		submit.setActionCommand("chat_submit_" + id);
		reset = new JButton("Reset");
		chooser = new JColorChooser(Color.BLACK);

		JLabel label = new JLabel("Message:");
		label.setFont(new Font(label.getFont().getName(), Font.BOLD, 14));
		JScrollPane jScrollPane1 = new JScrollPane(messageInput);
		JList<SocketThread> participantList = new JList<SocketThread>(conversation.getParticipants());

		messageInput.setLineWrap(true);
		messageInput.setWrapStyleWord(true);
		messageInput.setColumns(24);
		messageInput.setRows(5);

		JScrollPane window = new JScrollPane(messageWindow);
		ColorPalette cp = new ColorPalette(chooser);
		//FontfaceButton cp = new FontfaceButton("B", "<fetstil>", "</fetstil>");

		messageWindow.setPreferredSize(new Dimension(400, 300));
		messageWindow.setEditable(false);
		messageWindow.setContentType("text/html; charset= UTF-8");
		messageWindow.setEditorKit(conversation.getEditorKit());
		messageWindow.setDocument(conversation.getDocument());		

		jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		GroupLayout layout = new GroupLayout(this);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);

		setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(window)
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(label)
								.addComponent(cp))
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(jScrollPane1)
								.addGroup(layout.createSequentialGroup()
										.addComponent(submit)
										.addComponent(reset)))
						.addComponent(participantList))
		);

		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(window)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addComponent(label)
								.addComponent(cp))
						.addGroup(layout.createSequentialGroup()
								.addComponent(jScrollPane1)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(submit)
										.addComponent(reset)))
						.addComponent(participantList))
				);
	}

	public void addListeners(Conversation c) {
		submit.addActionListener(c);
	}

	public int getId() {
		return id;
	}
	
	private class FontfaceButton extends JPanel {
		public FontfaceButton(String text, final String tag, final String endTag) {
			JLabel label = new JLabel(text);
			
			add(label);
			addMouseListener(new MouseAdapter() {

				@Override
				public void mousePressed(MouseEvent e) {
					if (messageInput.getSelectionEnd() != messageInput.getSelectionStart()) {
						messageInput.insert(endTag, messageInput.getSelectionEnd());
						messageInput.insert(tag, messageInput.getSelectionStart());
					} else {
						messageInput.insert(tag + endTag, messageInput.getCaretPosition());
						messageInput.setCaretPosition(messageInput.getCaretPosition() - endTag.length());
					}
				}
			});
		}
	}
}
