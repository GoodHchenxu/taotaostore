package com.taotao.controller;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.junit.Test;

public class FTPtext {
	
	@Test
	public void textFtpclient() throws Exception{
		//创建一个ftpClient对象
		FTPClient ftpClient = new FTPClient();
		//创建ftp连接
		ftpClient.connect("192.168.0.110",21);
		//登陆ftp服务器，使用用户名和密码
		ftpClient.login("ftpuser", "123");
		//上传文件
		//第一个参数：服务器端文件名 第二个参数：要上传的文件的IO流
		//读取本地文件
		FileInputStream inputStream = new FileInputStream(
				new File("C:\\Users\\Haochenxu\\Pictures\\hello.jpg"));
		//设置上传路径
		ftpClient.changeWorkingDirectory("/home/ftpuser/www/images");
		//修改文件上传的格式
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		ftpClient.storeFile("hello1.jpg", inputStream);
		//关闭连接
		ftpClient.logout();
		
	}
}
