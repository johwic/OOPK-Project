package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.Conversation;
import model.SocketThread;

public class TabInterface extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3884551684205295307L;

	private final JTextArea messageInput;
	private final JButton submit;
	private final JButton reset;
	private final JButton quit;
	private final JTextPane messageWindow;
	private final Conversation conversation;
	private final JColorChooser chooser;

	private String title;

	public TabInterface(Conversation c) {

		conversation = c;

		messageInput = new JTextArea();
		messageWindow = new JTextPane();

        // buttons
		submit = new JButton("Submit");
		submit.setActionCommand("submit");
		reset = new JButton("Reset");
		quit = new JButton("Disconnect");
		quit.setActionCommand("disconnect");
		chooser = new JColorChooser(Color.BLACK);
		
		JTextField userName = new JTextField(10);
		userName.getDocument().addDocumentListener(conversation.getDocumentListener("user_name"));

		JLabel label = new JLabel("Message:");
		label.setFont(new Font(label.getFont().getName(), Font.BOLD, 14));
		
		JLabel nameLabel = new JLabel("Name:");
		nameLabel.setFont(new Font(nameLabel.getFont().getName(), Font.BOLD, 14));
		
		JScrollPane jScrollPane1 = new JScrollPane(messageInput);
		JList<SocketThread> participantList = new JList<SocketThread>(conversation.getParticipants());
		participantList.addListSelectionListener(conversation);

		messageInput.setLineWrap(true);
		messageInput.setWrapStyleWord(true);
		messageInput.setColumns(24);
		messageInput.setRows(5);

		JScrollPane window = new JScrollPane(messageWindow);
		ColorPalette cp = new ColorPalette(chooser);
		FontfaceButton bold = new FontfaceButton("B", "<fetstil>", "</fetstil>");
		FontfaceButton italics = new FontfaceButton("I", "<kursiv>", "</kursiv>");

		messageWindow.setPreferredSize(new Dimension(400, 300));
		messageWindow.setEditable(false);
		messageWindow.setContentType("text/html; charset= UTF-8");
		messageWindow.setEditorKit(conversation.getEditorKit());
		messageWindow.setDocument(conversation.getDocument());
		
		submit.addActionListener(conversation);
		quit.addActionListener(conversation);
		reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				messageInput.setText(null);
				chooser.setColor(Color.BLACK);
			}
		});
		
		messageInput.getDocument().addDocumentListener(conversation.getDocumentListener("message_text"));
		chooser.getSelectionModel().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				StringBuilder sb = new StringBuilder("#");
				Color c = chooser.getColor();
				
				if (c.getRed() < 16) sb.append('0');
				sb.append(Integer.toHexString(c.getRed()));

				if (c.getGreen() < 16) sb.append('0');
				sb.append(Integer.toHexString(c.getGreen()));

				if (c.getBlue() < 16) sb.append('0');
				sb.append(Integer.toHexString(c.getBlue()));
				
			    conversation.setUserInput("text_color", sb.toString());
			}
		});

		jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		GroupLayout layout = new GroupLayout(this);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);

		setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(window)
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(nameLabel)
								.addComponent(label)
								.addComponent(cp))
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(userName)
								.addComponent(jScrollPane1)
								.addGroup(layout.createSequentialGroup()
										.addComponent(submit)
										.addComponent(reset)
										.addComponent(quit)))
						.addComponent(participantList))
		);

		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(window)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addComponent(nameLabel)
								.addComponent(label)
								.addComponent(cp))
						.addGroup(layout.createSequentialGroup()
								.addComponent(userName)
								.addComponent(jScrollPane1)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(submit)
										.addComponent(reset)
										.addComponent(quit)))
						.addComponent(participantList))
				);
	}

	public String getTitle() {
		return title;
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
