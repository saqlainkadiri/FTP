package rjsk.upload.ftp;

import java.io.File;
import java.io.FileInputStream;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class UploadTask extends SwingWorker<Void, Void> {
	private static final int BUFFER_SIZE = 4096;
	
	private String host;
	private int port;
	private String username;
	private String password;
	
	private File uploadFile;
	
	public UploadTask(String host, int port, String username, String password,
			File uploadFile) {
		this.host = host;
		this.port = port;
		this.username = username;
		this.password = password;
		this.uploadFile = uploadFile;
	}

	@Override
	protected Void doInBackground() throws Exception {
		FTPUtility util = new FTPUtility(host, port, username, password);
		try {
			util.connect();
			util.uploadFile(uploadFile, "//");
			
			FileInputStream inputStream = new FileInputStream(uploadFile);
			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;
			long totalBytesRead = 0;
			int percentCompleted = 0;
			long fileSize = uploadFile.length();

			while ((bytesRead = inputStream.read(buffer)) != -1) {
				util.writeFileBytes(buffer, 0, bytesRead);
				totalBytesRead += bytesRead;
				percentCompleted = (int) (totalBytesRead * 100 / fileSize);
				setProgress(percentCompleted);
			}

			inputStream.close();
			
			util.finish();
		} catch (FTPException ex) {
			JOptionPane.showMessageDialog(null, "Error uploading file: " + ex.getMessage(),
					"Error", JOptionPane.ERROR_MESSAGE);			
			ex.printStackTrace();
			setProgress(0);
			cancel(true);			
		} finally {
			util.disconnect();
		}
		
		return null;
	}

	@Override
	protected void done() {
		if (!isCancelled()) {
			JOptionPane.showMessageDialog(null,
					"File has been uploaded successfully!", "Message",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}	
}