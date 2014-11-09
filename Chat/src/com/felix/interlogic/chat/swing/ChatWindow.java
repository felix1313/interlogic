package com.felix.interlogic.chat.swing;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import javax.swing.JScrollBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;

public class ChatWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JButton btnSend;
	private String userName;
	private String messagePrefix;
	private DataOutputStream output;
	private JScrollPane scrollPane;
	private JTextArea textArea;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
		this.messagePrefix = "<" + userName + ">: ";
	}

	public void write(String s) {
		this.textArea.append(s + "\n");
	}

	public ChatWindow(DataOutputStream outp) {
		this();
		enterWindow();
		this.output = outp;
	}

	private void enterWindow() {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				ChatEnter enter = new ChatEnter(ChatWindow.this);
				enter.setVisible(true);
			}
		});
	}

	private ChatWindow() {
		initUI();
	}

	private void initUI() {
		setTitle("Chat");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow][]",
				"[::300px,grow][30px:n:30px]"));

		scrollPane = new JScrollPane();
		contentPane.add(scrollPane, "cell 0 0,grow");

		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);

		textField = new JTextField();
		contentPane.add(textField, "cell 0 1,growx,aligny center");
		textField.setColumns(10);

		btnSend = new JButton("Send");
		contentPane.add(btnSend, "cell 1 1");

		this.getRootPane().setDefaultButton(btnSend);
		btnSend.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					ChatWindow.this.output.writeUTF(generateMessage(textField
							.getText()));
					textField.setText("");
					JScrollBar vertical = scrollPane.getVerticalScrollBar();
					vertical.setValue(vertical.getMaximum());
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		});
	}

	private String generateMessage(String s) {
		return messagePrefix + s + "\n";

	}

}
