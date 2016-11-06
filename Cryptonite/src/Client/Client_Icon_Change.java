package Client;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import org.ini4j.Wini;

public class Client_Icon_Change {

    public static void change(String args) throws IOException {

    	System.out.println("icon change run");
        // Directory path here
        String path = args;

        String currentDir = System.getProperty("user.dir");
        String iconPath = currentDir + "\\_folder.ico";
        
      
        File temp = new File(currentDir+"\\temp.bat");
        temp.createNewFile();
        PrintWriter wr = new PrintWriter(temp,"MS949");
        //wr.write("cd \""+ temp2.getParent() + "\"\r\n");
        wr.write("attrib +s \"" + path +"\"\r\n");
        //wr.write("cd \""+ path +"\"\r\n");
        //wr.write("copy /y \""+ iconPath + "\" \""+ path+"\\_folder.ico" +"\"\r\n");
        wr.write("echo [.ShellClassInfo] >>" + path +"\\desktop.ini" + "\r\n");
        wr.write("echo ConfirmFileOp=0 >>" + path +"\\desktop.ini" + "\r\n");
        wr.write("echo NoSharing=1 >>" + path +"\\desktop.ini" + "\r\n");
        wr.write("echo IconFile=" + iconPath + " >>" + path +"\\desktop.ini" + "\r\n");
        wr.write("echo IconIndex=0 >>" + path +"\\desktop.ini" + "\r\n");
        wr.write("echo InfoTip=Cryptonite >>" + path +"\\desktop.ini" + "\r\n");
        //wr.write("cmd.exe /D /U /C TYPE desktop.ini >>" + path +"\\desktop.ini" + "\r\n");
        //wr.write("del /F /Q " + path +"\\desktop.ini" + "\r\n");
        wr.write("attrib +S +H " + path +"\\desktop.ini" + "\r\n");
        wr.write("taskkill /f /im explorer.exe\r\n");
        wr.write("explorer");
        //wr.write("attrib +s +h " + path +"\\_folder.ico" + "\r\n");
        wr.flush();
        wr.close();
        
        Process p = Runtime.getRuntime().exec(currentDir+"\\temp.bat");
        //temp.delete();
        /*File folder = new File(path);


        File temp = new File(path+"\\desktop.ini");
        if (!temp.exists())
        	temp.createNewFile();
        
        File[] listOfFiles = folder.listFiles();
        
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                if ("ini".equals(getExtension(listOfFiles[i])))
                {
                	System.out.println("gdgdgdgdgd");
                    File theFile = listOfFiles[i];
                    Wini ini = new Wini(theFile);
                    String currentDir = System.getProperty("user.dir");
                    String iconPath = currentDir + "\\_folder.ico";
                    String field = iconPath + ",0";
                    ini.put(".ShellClassInfo", "IconResource", field);
                    
                    
                    Runtime.getRuntime().exec("attrib +h +s -a " + "\"" + theFile.getAbsolutePath() + "\"");
                    Runtime.getRuntime().exec("attrib +r " + "\"" + path + "\"");
                    ini.store(theFile);
  

                }
            }
        }*/
    }
    
    public static void restore(String args) throws IOException {

        // Directory path here
        String path = args;
        
        File folder = new File(path+"\\desktop.ini");
        folder.delete();
        Runtime.getRuntime().exec("attrib -r -s " + "\"" + path + "\"");
        
        System.out.println(path);
/*
        File folder = new File(path);
        if (!folder.exists())
        	return;
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                if ("ini".equals(getExtension(listOfFiles[i])))
                {
                	System.out.println("wwwwwwwwwwww");
                    File theFile = listOfFiles[i];
                    
                    Runtime.getRuntime().exec("attrib -h -s +a " + "\"" + theFile.getAbsolutePath() + "\"");
                    //Runtime.getRuntime().exec("attrib -r -s " + "\"" + path + "\"");
                    
                    Wini ini = new Wini(theFile);
                    ini.remove(".ShellClassInfo", "IconResource");
                    

                    ini.store(theFile);

                }
            }
        }*/
    }

    public static String getExtension(File theFile) {
        String extension = null;
        String fileName = theFile.getName();
        int i = fileName.lastIndexOf('.');

        if (i > 0 && i < fileName.length() - 1) {
            extension = fileName.substring(i + 1).toLowerCase();
        }

        if (extension == null) {
            return "";
        }
        return extension;
    }
}