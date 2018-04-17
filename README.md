# File Transfer Protocol
FTP Application using Apache Commons Net API and Java Swing Application Framework. 

## Technologies used :
1. Java
2. Swing Framework
3. Apache Commons Net API

## Main Features : 
1. Uploading File using FTP
2. Downloading File using FTP
3. Basic FTP Functions

## Steps of Installation 
1. Download or Clone this "FTP" repository
2. Download 'commons-net-3.6.jar' file from here or any other latest version from the Internet.
3. In Eclipse IDE, right click on the project and go to 'Build Path' and click 'Configure Build Path'.
4. Click on 'Add Library' and add JUnit to the Java Build Path.
5. Click on 'Add External JARs' and add the downloaded 'commons-net-3.6.jar' to the Java Build Path.
6. Click Apply and then OK.
7. Now the Project is successfully configured and ready to run.

## Steps to setup FTP Server using vsftpd on your Local Ubuntu machine or any Remote machine (like AWS EC2 Linux Instance)
1. Installing vsftpd  
 a) sudo apt-get update  
 b) sudo apt-get install vsftpd  
2. Backup vsftpd.conf  
  sudo cp /etc/vsftpd.conf /etc/vsftpd.conf.orig  
3. Checking Firewall Status  
  sudo ufw status  
4. If Firewall is Active    
 a) sudo ufw allow 20/tcp  
 b) sudo ufw allow 21/tcp  
 c) sudo ufw allow 990/tcp  
 d) sudo ufw allow 40000:50000/tcp  
5. Adding a User (for FTP)  
 a) sudo adduser s1  
 b) passwd s1
6. Preparing the User Directory  
 a) sudo mkdir /home/s1
 b) sudo chown nobody:nogroup /home/s1  
 c) sudo chmod a-w /home/s1
 d) sudo ls -la /home/s1     
7. Create User Directory for Uploads    
 a) sudo chown s1:s1 /home/s1
 b) sudo ls -la /home/s1
8. Add a Test File  
 echo "vsftpd test file" | sudo tee /home/s1/SampleText.txt     
9. Configuring FTP Access  
 sudo nano /etc/vsftpd.conf    
  
 Start by opening the config file to verify that the settings in your configuration match those below:  
 ...  
 anonymous_enable=NO  
 write_enable=YES  
 local_enable=YES  
 chroot_local_user=YES  
 user_sub_token=s1  
 local_root=/home/s1/ftp  
 pasv_min_port=40000  
 pasv_max_port=50000  
 listen_port=45000  
 userlist_enable=YES  
 userlist_file=/etc/vsftpd.userlist  
 userlist_deny=NO  
 ...  
 When you're done making the changes, save and exit the file.  
 
10. Add User to userlist  
   echo "s1" | sudo tee -a /etc/vsftpd.userlist  
11. Restart vsftpd 
   sudo systemctl restart vsftpd    
12. Testing FTP is configured on Server
   a) If FTP Server is configured on a local machine run the following command on terminal 
   ftp -p localhost
   b) If FTP Server is configured on a remote machine run the following command on terminal 
   ftp -p <<ipaddress_of_remote_machine>>
   
   ![Testing on Server](/screenshots/localhost_test.png)  
13. Downloading files
	get SampleText.txt

![GET](/screenshots/get.png)
	
14. Uploading files
	put SampleText.txt test.txt 
	
	![PUT](/screenshots/put.png)
	
15. Conclusion  
	If you get proper acknowledgement as shown in steps 12, 13 and 14 then FTP Server setup on Ubuntu is successful. 

## Steps of Running the Project
1. Run  
	a) 'SwingFileDownloadFTP.java' as Java Application for downloading files using FTP.  
	b) 'SwingFileUploadFTP.java' as Java Application for uploading files using FTP.  
2. In Host Field, enter  
	a) 'localhost' or '127.0.0.1' if FTP Server is installed on the same machine.  
	b) SERVER_PUBLIC_IP if FTP Server is manually configured on a remote machine or hosted on Amazon Web Services EC2 Instance, etc.    
	c) SERVER_HOST_NAME if FTP Server is a free FTP Service Provider like DriveHQ(drivehq.com),etc.  
3. In Port Field, enter 21(works in most cases).
4. Username and Password depend on the FTP Server selected.
5. In Download Path Field, enter the FTP Server path of the file you want to download. 

## Screenshots
### File Upload
![File Upload](/screenshots/localhost_upload.png )

### File Download
![File Download](/screenshots/localhost_download.png)
