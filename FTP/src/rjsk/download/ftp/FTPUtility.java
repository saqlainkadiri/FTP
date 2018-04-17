package rjsk.download.ftp;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FTPUtility {

	private String host;
	private int port;
	private String username;
	private String password;

	private FTPClient ftpClient = new FTPClient();
	private int replyCode;

	private InputStream inputStream;

	public FTPUtility(String host, int port, String user, String pass) {
		this.host = host;
		this.port = port;
		this.username = user;
		this.password = pass;
	}

	public void connect() throws FTPException {
		try {
			ftpClient.connect(host, port);
			replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				throw new FTPException("FTP serve refused connection.");
			}

			boolean logged = ftpClient.login(username, password);
			if (!logged) {
				// failed to login
				ftpClient.disconnect();
				throw new FTPException("Could not login to the server.");
			}

			ftpClient.enterLocalPassiveMode();

		} catch (IOException ex) {
			throw new FTPException("I/O error: " + ex.getMessage());
		}
	}

	public long getFileSize(String filePath) throws FTPException {
		try {
			FTPFile file = ftpClient.mlistFile(filePath);
			if (file == null) {
				throw new FTPException("The file may not exist on the server!");
			}
			System.out.println(file);
			System.out.println(file.getSize());
			return file.getSize();
		} catch (IOException ex) {
			throw new FTPException("Could not determine size of the file: "
					+ ex.getMessage());
		}
	}

	public void downloadFile(String downloadPath) throws FTPException {
		try {
			
            ftpClient.enterLocalPassiveMode();
			boolean success = ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			if (!success) {
				throw new FTPException("Could not set binary file type.");
			}

			inputStream = ftpClient.retrieveFileStream(downloadPath);

			if (inputStream == null) {
				throw new FTPException(
						"Could not open input stream. The file may not exist on the server.");
			}
		} catch (IOException ex) {
			throw new FTPException("Error downloading file: " + ex.getMessage());
		}
	}

	public void finish() throws IOException {
		inputStream.close();
		ftpClient.completePendingCommand();
	}

	public void disconnect() throws FTPException {
		if (ftpClient.isConnected()) {
			try {
				if (!ftpClient.logout()) {
					throw new FTPException("Could not log out from the server");
				}
				ftpClient.disconnect();
			} catch (IOException ex) {
				throw new FTPException("Error disconnect from the server: "
						+ ex.getMessage());
			}
		}
	}

	public InputStream getInputStream() {
		return inputStream;
	}
}