package dot.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/*
 * 类类
 * 认为每个leafPath对应一个类，类名为该目录名
 * 该路径下，<init>存有该类中声明的变量，其他dot为该类中的方法
 */
public class ClassData {

	private String dirPath;//类所在路径
	private String classID;//类的ID
	private VarsInitData vars;//类中声明的所有变量
	private ArrayList<MethodData> methods;//类中实现的所有方法
	private ArrayList<String> methodNames;
	private boolean debug = false;
	
	public ClassData(String filePath){
		File dir = new File(filePath);
		dirPath = filePath;
		try {
			String dirStr = dir.getCanonicalPath();
			int pos0 = dirStr.lastIndexOf("output/dot/") + 11;
			pos0 = dirStr.indexOf('/', pos0) + 1;
			classID = dirStr.substring(pos0);//getName();////////////////////////////////////////20140619
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		vars = new VarsInitData(filePath);//
		setMethodNames(dir.listFiles());
		if(debug) System.out.println("[log] setMethodNames(" + methodNames.toString() + ");");
		findMethods(dir.listFiles(), methodNames);
	}
	
	/*public static void findVars(){
		
	}*/
	
	private void findMethods(File[] files, ArrayList<String> methodList){
		methods = new ArrayList<MethodData>();
		for(File f : files){
			if(f.isDirectory()) continue;
			if(f.getName().indexOf("<init>") >= 0 || f.getName().indexOf(".dot") < 0 || f.getName().indexOf("[new]") > 0){
				continue;
			}
			if(debug) System.out.println("[log] new MethodData(" + f.getAbsolutePath() + ");");
			methods.add(new MethodData(classID, f.getAbsolutePath(), vars.getVars(), methodList));
		}
	}
	
	public String getDir(){
		if(dirPath.equals(null))
			return "unknown";
		
		return dirPath;
	}
	
	public String getID(){
		if(classID.equals(null))
			return "unknown";
		
		return classID;
	}
	
	public static ClassData getClassDataByID(ClassData[] classes, String id){
		for(ClassData classdt : classes){
			if(classdt.getID().equalsIgnoreCase(id))
				return classdt;
		}
		return null;
	}
	
	public HashMap<String, String> getVars(){
		//ArrayList<String> varStrs = new ArrayList<String>();
		return vars.getVars();
	}
	
	public String[] getVarNames(){
		//ArrayList<String> varStrs = new ArrayList<String>();
		if(vars == null || vars.getVars() == null || vars.getVars().isEmpty()) return null;
		return vars.getVars().keySet().toArray(new String[0]);
	}
	
	private void setMethodNames(File[] files){
		methodNames = new ArrayList<String>();
		for(File f : files){
			if(f.isDirectory()) continue;
			if(f.getName().indexOf("<init>") >= 0 || f.getName().indexOf(".dot") < 0 || f.getName().indexOf("[new]") > 0){
				continue;
			}
			String[] methodInfo = f.getName().split("-");
			//if(methodInfo.length != 3)
			if(true)
				//methodNames.add(f.getName().substring(0, f.getName().indexOf(".dot")));
				methodNames.add(classID + "->" + f.getName().substring(0, f.getName().indexOf(".dot")));//modified in 20140709
			else{
				methodNames.add(classID + "->" + methodInfo[0]);//modified in 20140414
				//methodType = methodInfo[1];
				//methodKind = methodInfo[2];
			}
		}
	}
	
	public String[] getMethodNames(){
		return methodNames.toArray(new String[0]);
	}
	
	public MethodData getMethodDataByName(String methodnm){
		for(MethodData md : methods){
			if(md.getID().equals(methodnm)) return md;
		}
		return null;
	}

    public static void generateNewDOTs(File[] files){
    	//该路径代表一个class，路径下的每个DOT文件代表一个method
    	
    	//先找到<init>文件，得到变量集合
    	
    	//解析每个DOT文件，即每个method，得到
    }
    
    //这里只是为了连缀各个Method的CB，不是严格的json
    public String toJson(){
    	StringBuffer r = new StringBuffer("");
    	for(MethodData methoddt : methods){
    		//System.out.println("fuck: " + methoddt.getID());
    		r.append(methoddt.toJson());
    	}
    	return r.toString();
    }
}
