package dot.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class VarsInitData {

	private String classStr;
	private HashMap<String, String> vars;
	private OneCodeblock initblock;
	private boolean debug = false;
	
	public VarsInitData(String filePath){
		File dir = new File(filePath);
		classStr = dir.getName();
		//System.out.println("classStr: " + classStr);
		File[] files = dir.listFiles();
		for(File f : files){
			if(f.getName().indexOf("<init>") >= 0 && f.getName().indexOf(".dot") >= 0 && f.getName().indexOf("[new]") < 0){
				parseVars(f.getAbsoluteFile());
				return;
			}
		}
	}
	
	private void parseVars(File f){
		vars = new HashMap<String, String>();
		//System.out.println("parseVars: " + f.getAbsolutePath());
		try {
            String encoding = "UTF-8";
            if(f.isFile() && f.exists()){
                InputStreamReader read = new InputStreamReader(new FileInputStream(f), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = bufferedReader.readLine();
                initblock = new OneCodeblock(lineTxt, classStr, null, new HashMap<String, String>(), null);//这里varList、methodList为空好了
                read.close();
	        }
            else{
            	if(debug) System.out.println("no such a file: " + f.getAbsolutePath());
	        }
        }
        catch(Exception e){
        	//System.out.println("failed to read: " + f.getAbsolutePath());
            e.printStackTrace();
        }
		
		//从initblock中得到vars
		ArrayList<StringBuffer> cmdsToGetVars = initblock.getCmds();
		for(StringBuffer cmd : cmdsToGetVars){
			String varGot = VarOpItem.parseVarFromInit(classStr, cmd);
			int space = varGot.indexOf(" ");
			String varType = varGot.substring(0, space);
			String varName = varGot.substring(space + 1);
			vars.put(varName, varType);
			if(debug) System.out.println("[log] a new var: (" + varType + ")" + varName);
		}
	}
	
	public HashMap<String, String> getVars(){
		return vars;
	}
}
