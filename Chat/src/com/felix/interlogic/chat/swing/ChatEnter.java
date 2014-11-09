package com.felix.interlogic.chat.swing;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

public class ChatEnter extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField nameText;
	private JButton submit;
	private ChatWindow parent;

	public ChatEnter(ChatWindow parent) {
		this.parent = parent;
		initUI();
		
	}

	private void initUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[][][][][grow]",
				"[][][][][][][][][][]"));

		JLabel lblEnter = new JLabel("Enter Your Name");
		contentPane.add(lblEnter, "cell 2 4");

		nameText = new JTextField();
		nameText.setText("");
		contentPane.add(nameText, "cell 4 4,alignx left");
		nameText.setColumns(10);

		submit = new JButton("Submit");
		submit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							parent.setUserName(nameText.getText());
							parent.setVisible(true);
							ChatEnter.this.setVisible(false);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});

		this.getRootPane().setDefaultButton(submit);
		contentPane.add(submit, "cell 2 9 3 1,growx");
	}
}
