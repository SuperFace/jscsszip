package com.compressor.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;

import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

import com.yahoo.platform.yui.compressor.CssCompressor;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;

public class jscsszip {
	//换行符指定的列数，-1不换行
	private static int linebreakpos = -1;
	//是否混淆
	private static boolean munge = true;
	//是否显示提示消息，警告，注释
	private static boolean verbose = false;
	//保留所有分号
	private static boolean preserveAllSemiColons = false;
	//是否禁用优化
	private static boolean disableOptimizations = false;
	 
	 
	 public void testMain(File dir) throws Exception{
	     checkFile(dir);
	 }
	 public void checkFile(File file) throws Exception{
	    if(file.getName().endsWith(".svn")) return;
	    if(file.isFile()){
	       jsZip(file);
	       return;
	    }
	    if(file.isDirectory()){
		   File[] files=file.listFiles();
		   if(files==null||files.length==0) return;
		   for(File f:files){
			   if(f.getName().endsWith(".svn")) continue;
			   if(f.isFile()){
			      jsZip(f);
			      continue;
			   }
			   checkFile(f);
		   }
	   }
	 }
	 public void jsZip(File file) throws Exception{
	    String fileName=file.getName();
	    System.out.println(fileName);
	    if(fileName.endsWith(".js")==false&&fileName.endsWith(".css")==false){
	        return;
	    }
	    Reader in=new FileReader(file);
	    String filePath=file.getAbsolutePath();
	    File tempFile=new File(filePath+".tempFile");//压缩过程创建的临时文件
	    Writer out=new FileWriter(tempFile);
	    if(fileName.endsWith(".js")){
	        JavaScriptCompressor jscompressor=new JavaScriptCompressor(in, new ErrorReporter() {
	             public void warning(String message, String sourceName, int line, String lineSource, int lineOffset) {
	                 if (line < 0) {
	                     System.err.println("\n[WARNING]" + message);
	                 } else {
	                     System.err.println("\n[WARNING]" + line + ':' + lineOffset + ':' + message);
	                 }
	             }
	             public void error(String message, String sourceName, int line, String lineSource, int lineOffset) {
	                 if (line < 0) {
	                     System.err.println("\n[ERROR] " + message);
	                 } else {
	                     System.err.println("\n[ERROR] " + line + ':' + lineOffset + ':' + message);
	                 }
	             }
	             public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource, int lineOffset) {
	                 error(message, sourceName, line, lineSource, lineOffset);
	                 return new EvaluatorException(message);
	             }
	        });
	       jscompressor.compress(out, linebreakpos, munge, verbose, preserveAllSemiColons, disableOptimizations);
	        //jscompressor.compress(out, linebreakpos, true, false, false, false);
	       System.out.println(fileName+" 压缩完毕=========");
	       
	      /* BufferedReader bf = new BufferedReader(new FileReader(tempFile));
	       String content = "";
	       StringBuilder sb = new StringBuilder();
	       while(content != null){
	          content = bf.readLine();
	          if(content == null){
	              break;
	          }
	          sb.append(content.trim());
	       }
	       bf.close();
	       ByteArrayInputStream stream = new ByteArrayInputStream((JavaScriptUtil.obfuscateScript(sb.toString())).getBytes());
	       File _tempFile=new File(filePath+".tempFile1");//压缩过程创建的临时文件
	       OutputStream os = new FileOutputStream(file);
	       int bytesRead = 0;
	       byte[] buffer = new byte[8192];
	       while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
	       os.write(buffer, 0, bytesRead);
	       }
	       os.close();
	       stream.close();
	       tempFile = _tempFile;*/
	    }else if(fileName.endsWith(".css")){
	       CssCompressor csscompressor = new CssCompressor(in);
	       csscompressor.compress(out, linebreakpos);
	       System.out.println(fileName+" 压缩完毕=========");
	    }
	    out.close();
	    in.close();
	    file.delete();
	    tempFile.renameTo(file);
	    tempFile.delete();
	 }
	 
	 public static void main(String[] args){
		 jscsszip tem = new jscsszip();
		 try {
			 File dir=new File("D:/eclipse-jp-x86_64/wwwjp/jscsszip/src/test/");
			 tem.testMain(dir);
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
	 }
}
