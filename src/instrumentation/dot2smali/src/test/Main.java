package test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File; 
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import dot.data.ClassData;
import dot.data.VarOpItem;
import json.Classes2Json;

public class Main {
	
	public static String dotRootPath;
	public static String dotOutputPath;
	public static String smaliRootPath;
	public static String smaliOutputPath;
	public static String jsonOutputName;
    private static ArrayList<String> classes = new ArrayList<String>();
    private static ArrayList<dot.data.ClassData> classesData = new ArrayList<dot.data.ClassData>();
	public static boolean debuggable = true;
    
    public static void main(String[] args) throws Exception {
    	String appName = args[0];
    	dotRootPath = "input/dot/" + appName + "/";
    	dotOutputPath = "output/dot/" + appName + "/";
    	smaliRootPath = "input/smali/" + appName + "/";
    	smaliOutputPath = "output/smali/" + appName + "/";
    	jsonOutputName = "../shareIO/" + appName + ".json";
    	
    	VarOpItem.initHashMap();

        System.out.println("[log] getLeafPaths(" + dotRootPath + ");");
        clearDir(dotOutputPath);//
        copyDir(dotRootPath, dotOutputPath);
        long t = System.currentTimeMillis();
        getLeafPaths(dotOutputPath, 0, debuggable);
        System.out.println(System.currentTimeMillis() - t);
        System.out.println("[log] classes obtained from dots!");
        
        System.out.println("[log] getLeafPaths(" + smaliRootPath + ");");
        clearDir(smaliOutputPath);//
        copyDir(smaliRootPath, smaliOutputPath);
        t = System.currentTimeMillis();
        File[] debagFilesUnderRoot = new File(smaliOutputPath).listFiles();
        if(debagFilesUnderRoot.length == 0) {// || !debagFilesUnderRoot[0].isDirectory()) {
        	System.out.println("fail to find smali generated just now!");
        	return;
        }
        //getLeafPaths(debagFilesUnderRoot[0].getAbsolutePath() + "/smali/" + (args.length > 1 ? parseRelativePath(args[1]) : "com/"), 1);//<-- 要插桩的子目录
        getLeafPaths(new File(smaliOutputPath).getAbsolutePath() + "/smali/" + (args.length > 1 ? parseRelativePath(args[1]) : "com/"), 1, debuggable);//<-- 要插桩的子目录
        System.out.println(System.currentTimeMillis() - t);
        System.out.println("[log] classes modified in smalis!");
        
        Classes2Json cjson = new Classes2Json(jsonOutputName, classesData);
        System.out.println("[log] classes transformed into json!");
    }
    
    public static String parseRelativePath(String p) {
    	String r = p + ((p.length() == 0 || (p.length() > 0 && p.charAt(p.length() - 1) != '/')) ? "/" : "");
    	if(r.charAt(0) == '/') return r.substring(1);
    	return r;
    }
    
    public static void getLeafPaths(String strPath, int choice, boolean debug) {
        File dir = new File(strPath);
        File[] files = dir.listFiles();
        if (files == null)
            return;
        
        //cunzai wenti! Jiyou wenjian youyou wenjianjia shi, wenjian huibei hulue!
        if(dirIsleafPath(files)){
        	if(choice == 0){
            	if(debug) System.out.println("[log] new dotClassData(" + strPath + ");");
            	dot.data.ClassData classdt = new ClassData(strPath);//视为leafPath，生成该类下的所有数据
            	classes.add(classdt.getID());
            	classesData.add(classdt);//a class of dot
        	}
        	else{
        		dot.data.ClassData[] clses = classesData.toArray(new dot.data.ClassData[0]);
        		//System.out.println(clses.length);
        		for(File sml : files){
        			if(sml.getName().indexOf(".smali") < 0) continue;
        			if(debug) System.out.println("[log] new smaliClassData(" + sml.getAbsolutePath() + ");");
	            	try {
						smali.data.ClassData classdt = new smali.data.ClassData(sml.getAbsolutePath(), dot.data.ClassData.getClassDataByID(clses, 
								sml.getCanonicalPath().substring(sml.getCanonicalPath().lastIndexOf("smali/") + 6, sml.getCanonicalPath().indexOf(".smali"))));
								//sml.getName().substring(0, sml.getName().indexOf(".smali"))));//a class of smali
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            			
	            	//
        		}
        	}
        	//return;
        }
        
        for (File f : files) {
            if (f.isDirectory()) {
            	if(debug) System.out.println("[log] getLeafPaths(" + f.getAbsolutePath() + ");");
            	getLeafPaths(f.getAbsolutePath(), choice, debug);
            }
        }
    }
    
    //判断files所在的路径是否是leafPath
    //gaiwei panduan files zhong shifou you wenjian!
    public static boolean dirIsleafPath(File[] files){
    	for (File f : files) {
    		if(!f.isDirectory())
    			return true;
    	}
    	return false;
    }
    
    //copy a folder
    public static void copyDir(String sourcsPath, String targetPath) throws Exception {
    	File sourceFile = new File(sourcsPath);
    	if(sourceFile.isDirectory()){
    		File targetFile = new File(targetPath);
    		if(!targetFile.exists())
    			targetFile.mkdir();
    		File[] sourceFiles = sourceFile.listFiles();
    		for (File f : sourceFiles){
    			copyDir(f.toString(), targetPath + "/" + f.getName());
    		}
    	}
    	else{
    		copy(sourcsPath,targetPath);
    	}
    }

    //拷贝文件
    public static void copy(String path1, String path2) throws IOException {
    	DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(path1)));
    	byte[] date = new byte[in.available()];
    	in.read(date);
    	DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(path2)));
    	out.write(date);
    	in.close();
    	out.close();
    }
    
    //move a folder
	public static void moveDir(String oldPath, String newPath) {
		try {
			File srcFolder = new File(oldPath);
			File destFolder = new File(newPath);
			File newFile = new File(destFolder.getAbsoluteFile() + "\\"	+ srcFolder.getName());
			srcFolder.renameTo(newFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//delete a folder
	public static void deleteDir(String folderPath) {
		try {
			clearDir(folderPath); //删除完里面所有内容
			File myFilePath = new File(folderPath);
			myFilePath.delete(); //删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//delete files in a folder
	public static boolean clearDir(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				clearDir(path + "/" + tempList[i]);//先删除文件夹里面的文件
				deleteDir(path + "/" + tempList[i]);//再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}
    
}