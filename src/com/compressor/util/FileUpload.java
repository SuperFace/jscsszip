package com.compressor.util;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class FileUpload {
	public static void main(String[] args) {
		FileUpload.uploadAndConfusedJs("D:/eclipse-jp-x86_64/wwwjp/jscsszip/src/test/", "D:/eclipse-jp-x86_64/wwwjp/jscsszip/src/test/");
	}
	/**
	 * @Title uploadAndConfusedJs
	 * @Description: 上传并混淆JS文件
	 * @param uploadUrl 上传路径
	 * @param saveUrl 保存路径
	 * @throws
	 */
	public static void uploadAndConfusedJs(String uploadUrl, String saveUrl) {
		if("".equals(uploadUrl)||"".equals(uploadUrl)){
			System.out.println("java混淆JS方法提示信息:=======上传或保存JS文件路径不能为空!");
			return;
		}
		File inputFile = new File(uploadUrl);
		if(inputFile.isDirectory()){
			File[] inputFiles=inputFile.listFiles();
			if(inputFiles==null||inputFiles.length==0) return;
			for(File f:inputFiles){
			   if(f.getName().endsWith(".js")){
				   if(f.isFile()){
					   String saveContent=getJsContent(f);
						
						try {
							FileOutputStream output = new FileOutputStream(saveUrl+f.getName().replace(".js", ".min.js"));
							DataOutputStream dos=new DataOutputStream(output);
							byte b[]=saveContent.getBytes();
							dos.write(b);
							dos.close();
							System.out.println(f.getName() + "转换完成。。。");
						} catch (Exception e) {
							e.printStackTrace();
						}
				   }
			   }
		   }
		}
	}
	
	public static String getJsContent(File file) {
		String str="";
		try {
			DataInputStream dis=new DataInputStream(new FileInputStream(file));
			dis.read(new byte[3]);
			byte data[]=new byte[dis.available()];
			dis.readFully(data);
			str=new String(data,"UTF-8");
			dis.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return JavaScriptUtil.obfuscateScript(str);
	}
}
