package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;

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
	private final JColorChooser colorChooser;
	private final JFileChooser fileChooser;

	private String title;

	public TabInterface(Conversation c, String name) {

		conversation = c;
		title = c.toString();
		
		messageInput = new JTextArea();
		messageWindow = new JTextPane();
		DefaultCaret caret = (DefaultCaret) messageWindow.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        // buttons
		submit = new JButton("Submit");
		submit.setActionCommand("submit");
		reset = new JButton("Reset");
		quit = new JButton("Disconnect");
		quit.setActionCommand("disconnect");
		colorChooser = new JColorChooser(Color.BLACK);
		
		fileChooser = new JFileChooser();
		final JTextField filePathField = new JTextField(10);
		filePathField.setEditable(false);
		JButton fileChooserButton = new JButton("Choose a file");
		fileChooserButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
		        int returnVal = fileChooser.showOpenDialog(null);

		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		        	conversation.setUserInput("file_path", fileChooser.getSelectedFile().getPath());
		        	filePathField.setText(fileChooser.getSelectedFile().getPath());
		        }
			}
			
		});
		JButton send = new JButton("Send");
		send.setActionCommand("send_file");
		
		
		JTextField userName = new JTextField(10);
		userName.getDocument().addDocumentListener(conversation.getDocumentListener("user_name"));

		try {
			userName.getDocument().insertString(0, name, null);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
		JLabel msgInputLbl = new JLabel("Message:");
		msgInputLbl.setFont(new Font(msgInputLbl.getFont().getName(), Font.BOLD, 14));
		
		JLabel nameInputLbl = new JLabel("Name:");
		nameInputLbl.setFont(new Font(nameInputLbl.getFont().getName(), Font.BOLD, 14));
		
		JScrollPane msgInputPane = new JScrollPane(messageInput);
		
		JList<SocketThread> participantList = new JList<SocketThread>(conversation.getParticipants());
		participantList.addListSelectionListener(conversation);
		JScrollPane participantPane = new JScrollPane(participantList);
		participantList.setVisibleRowCount(3);
		
		JComboBox<String> cryptoList = new JComboBox<String>(conversation.getSupportedAlgorithms());
		cryptoList.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				JComboBox<String> src = (JComboBox<String>) e.getSource();
					
				conversation.setUserInput("selected_crypto", (String) src.getSelectedItem());
			}
			
		});
		JButton setCryptoBtn = new JButton("Set crypto");
		setCryptoBtn.setActionCommand("set_crypto");
		JTextField keyRequestMsg = new JTextField(10);
		keyRequestMsg.getDocument().addDocumentListener(conversation.getDocumentListener("key_request_msg"));
		JButton requestKeyBtn = new JButton("Request key");
		requestKeyBtn.setActionCommand("request_key");

		messageInput.setLineWrap(true);
		messageInput.setWrapStyleWord(true);
		messageInput.setColumns(24);
		messageInput.setRows(5);

		JScrollPane window = new JScrollPane(messageWindow);
		
		ColorPalette cp = new ColorPalette(colorChooser);
		ImageIcon b = new ImageIcon("text-bold-icon.png", "Bold");
		ImageIcon i = new ImageIcon("text-italic-icon.png", "Italics");
		FontfaceButton bold = new FontfaceButton(b, "<fetstil>", "</fetstil>");
		FontfaceButton italics = new FontfaceButton(i, "<kursiv>", "</kursiv>");
		
		cp.add(bold);
		cp.add(italics);

		messageWindow.setPreferredSize(new Dimension(400, 300));
		messageWindow.setEditable(false);
		messageWindow.setContentType("text/html; charset= UTF-8");
		messageWindow.setEditorKit(conversation.getEditorKit());
		messageWindow.setDocument(conversation.getDocument());
		
		submit.addActionListener(conversation);
		quit.addActionListener(conversation);
		send.addActionListener(conversation);
		setCryptoBtn.addActionListener(conversation);
		requestKeyBtn.addActionListener(conversation);
		reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				messageInput.setText(null);
				colorChooser.setColor(Color.BLACK);
			}
		});
		
		messageInput.getDocument().addDocumentListener(conversation.getDocumentListener("message_text"));
		colorChooser.getSelectionModel().addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				StringBuilder sb = new StringBuilder("#");
				Color c = colorChooser.getColor();
				
				if (c.getRed() < 16) sb.append('0');
				sb.append(Integer.toHexString(c.getRed()));

				if (c.getGreen() < 16) sb.append('0');
				sb.append(Integer.toHexString(c.getGreen()));

				if (c.getBlue() < 16) sb.append('0');
				sb.append(Integer.toHexString(c.getBlue()));
				
			    conversation.setUserInput("text_color", sb.toString());
			}
		});

		msgInputPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);	
		
		GroupLayout layout = new GroupLayout(this);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);

		setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(window)
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createSequentialGroup()
							.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
									.addComponent(nameInputLbl)
									.addComponent(msgInputLbl)
									.addComponent(cp, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
							.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
									.addComponent(userName)
									.addComponent(msgInputPane)
									.addGroup(layout.createSequentialGroup()
											.addComponent(submit)
											.addComponent(reset)
											.addComponent(quit))))
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(participantPane)
								.addGroup(layout.createSequentialGroup()
										.addComponent(filePathField)
										.addComponent(fileChooserButton)
										.addComponent(send))
								.addGroup(layout.createSequentialGroup()
										.addComponent(cryptoList)
										.addComponent(setCryptoBtn))
								.addGroup(layout.createSequentialGroup()
										.addComponent(keyRequestMsg)
										.addComponent(requestKeyBtn))))
		);

		layout.setVerticalGroup(layout.createSequentialGroup()
				.addComponent(window)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
							.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
									.addComponent(nameInputLbl)
									.addComponent(userName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
							.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
									.addGroup(layout.createSequentialGroup()
											.addComponent(msgInputLbl)
											.addComponent(cp))
									.addComponent(msgInputPane))
							.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
											.addComponent(submit)
											.addComponent(reset)
											.addComponent(quit)))
						.addGroup(layout.createSequentialGroup()
								.addComponent(participantPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(filePathField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(fileChooserButton)
										.addComponent(send))
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(cryptoList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(setCryptoBtn))
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addComponent(keyRequestMsg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(requestKeyBtn))))
				);
	}

	public String getTitle() {
		return title;
	}
	
	private class FontfaceButton extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1699601872432702002L;

		public FontfaceButton(ImageIcon icon, final String tag, final String endTag) {
			
			JLabel label = new JLabel(icon);
			
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
