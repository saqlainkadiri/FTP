package rjsk.download.ftp;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import net.codejava.swing.JFilePicker;

public class SwingFileDownloadFTP extends JFrame implements
		PropertyChangeListener {

	private JLabel labelHost = new JLabel("Host:");
	private JLabel labelPort = new JLabel("Port:");
	private JLabel labelUsername = new JLabel("Username:");
	private JLabel labelPassword = new JLabel("Password:");
	private JLabel labelDownloadPath = new JLabel("Download path:");

	private JTextField fieldHost = new JTextField(40);
	private JTextField fieldPort = new JTextField(5);
	private JTextField fieldUsername = new JTextField(30);
	private JPasswordField fieldPassword = new JPasswordField(30);
	private JTextField fieldDownloadPath = new JTextField(30);

	private JFilePicker filePicker = new JFilePicker("Save file to: ",
			"Browse...");

	private JButton buttonDownload = new JButton("Download");

	private JLabel labelFileSize = new JLabel("File size (bytes):");
	private JTextField fieldFileSize = new JTextField(15);

	private JLabel labelProgress = new JLabel("Progress:");
	private JProgressBar progressBar = new JProgressBar(0, 100);

	public SwingFileDownloadFTP() {
		super("Swing File Download from FTP server");

		// set up layout
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(5, 5, 5, 5);

		// set up components
		filePicker.setMode(JFilePicker.MODE_SAVE);
		filePicker.getFileChooser().setFileSelectionMode(
				JFileChooser.DIRECTORIES_ONLY);

		fieldFileSize.setEditable(false);

		buttonDownload.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				buttonDownloadActionPerformed(event);
			}
		});

		progressBar.setPreferredSize(new Dimension(200, 30));
		progressBar.setStringPainted(true);

		// add components to the frame
		constraints.gridx = 0;
		constraints.gridy = 0;
		add(labelHost, constraints);

		constraints.gridx = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1.0;
		add(fieldHost, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		add(labelPort, constraints);

		constraints.gridx = 1;
		add(fieldPort, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		add(labelUsername, constraints);

		constraints.gridx = 1;
		add(fieldUsername, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		add(labelPassword, constraints);

		constraints.gridx = 1;
		add(fieldPassword, constraints);

		constraints.gridx = 0;
		constraints.gridy = 4;
		add(labelDownloadPath, constraints);

		constraints.gridx = 1;
		add(fieldDownloadPath, constraints);

		constraints.gridx = 0;
		constraints.gridwidth = 2;
		constraints.gridy = 5;
		constraints.anchor = GridBagConstraints.WEST;

		add(filePicker, constraints);

		constraints.gridx = 0;
		constraints.gridy = 6;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		add(buttonDownload, constraints);

		constraints.gridx = 0;
		constraints.gridy = 7;
		constraints.gridwidth = 1;
		constraints.anchor = GridBagConstraints.WEST;

		add(labelFileSize, constraints);

		constraints.gridx = 1;
		add(fieldFileSize, constraints);

		constraints.gridx = 0;
		constraints.gridy = 8;
		add(labelProgress, constraints);

		constraints.gridx = 1;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		add(progressBar, constraints);

		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void buttonDownloadActionPerformed(ActionEvent event) {
		String host = fieldHost.getText();
		int port = Integer.parseInt(fieldPort.getText());
		String username = fieldUsername.getText();
		String password = new String(fieldPassword.getPassword());
		String downloadPath = fieldDownloadPath.getText();
		String saveDir = filePicker.getSelectedFilePath();

		progressBar.setValue(0);
		DownloadTask task = new DownloadTask(host, port, username, password,
				downloadPath, saveDir, this);
		task.addPropertyChangeListener(this);
		task.execute();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			int progress = (Integer) evt.getNewValue();
			progressBar.setValue(progress);
		}
	}

	void setFileSize(long fileSize) {
		fieldFileSize.setText(String.valueOf(fileSize));
	}

	public static void main(String[] args) {
		try {
			// set look and feel to system dependent
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new SwingFileDownloadFTP().setVisible(true);
			}
		});
	}
}