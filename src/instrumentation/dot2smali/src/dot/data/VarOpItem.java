package dot.data;

import java.util.ArrayList;
import java.util.HashMap;

/*
 * 变量操作指令类
 * 变量名、操作类型
 * 这里的变量操作是广义的，包含“调用类中声明的method”的情况……？
 */
public class VarOpItem {

	public static HashMap<String, Integer> operation2type;
	private String classID;
	private String varStr;//var/method名称
	private String varType;
	private String operation;
	private int type = -1;//0代表read，1代表write，2代表调用（method），-1表示不是变量操作
	private static String[] operationTypes = {"r", "w", "invoked"};
	
	//需不需要把varNameList也传参传进来呢？
	public VarOpItem(StringBuffer cmdStr, String className, HashMap<String, String> varList, ArrayList<String> methodList){
		classID = className;
		
		//if(cmdStr.indexOf("/" + className + ";->") < 0){
		if(cmdStr.indexOf(className + ";->") < 0){
			System.out.println("fuck classname: " + className);//<--
			System.out.println("fuck cmdstr: " + cmdStr);//<--
			
			varStr = "unknown";
			varType = "unknown";
			operation = "unknown";
			type = -1;
			return;
		}
		
		int bpos_cmd = cmdStr.indexOf(" ") + 1;
		int epos_cmd = cmdStr.indexOf(" ", bpos_cmd);
		String cmdWord = cmdStr.substring(bpos_cmd, epos_cmd);
		//System.out.println("fuck cmdword: " + cmdWord);//<--
		
		if(operation2type.containsKey(cmdWord)){
			type = operation2type.get(cmdWord);
			//System.out.println("fuck cmdword: " + cmdWord);//<--
			operation = operationTypes[type];
			int bpos_vs = cmdStr.indexOf(";->") + 3;
			int epos_vs = cmdStr.indexOf(" ", bpos_vs);
			varStr = cmdStr.substring(bpos_vs, epos_vs);
			if(varList == null || varList.isEmpty() || !varList.containsKey(varStr)) {
				//System.out.println("fuck cmdword: " + cmdWord);//<--
				type = -1;
			}
			varType = varList.get(varStr);//暂时不验证该var是否真的是该type
			return;
		}
		if(cmdWord.indexOf("invoke-") >= 0){
			int bpos_vs = cmdStr.indexOf(";->") + 3;
			int epos_vs_0 = cmdStr.indexOf("(", bpos_vs);
			int epos_vs_1 = cmdStr.indexOf(")", bpos_vs) + 1;
			System.out.println("[log] a cmdStr is: " + cmdStr);
			//if(methodList.contains(cmdStr.substring(bpos_vs, epos_vs_0)))
			if(false){
				varStr = cmdStr.substring(bpos_vs, epos_vs_1);//
				varType = "function";
				type = 2;
				operation = operationTypes[type];
				return;
			}
		}
		varStr = "unknown";
		varType = "unknown";
		operation = "unknown";
		type = -1;
	}
	
	public static void initHashMap(){
		operation2type = new HashMap<String, Integer>(0);
		
		operation2type.put("iget", 0);
		operation2type.put("iget-wide", 0);
		operation2type.put("iget-object", 0);
		operation2type.put("iget-boolean", 0);
		operation2type.put("iget-byte", 0);
		operation2type.put("iget-char", 0);
		operation2type.put("iget-short", 0);

		operation2type.put("iput", 1);
		operation2type.put("iput-wide", 1);
		operation2type.put("iput-object", 1);
		operation2type.put("iput-boolean", 1);
		operation2type.put("iput-byte", 1);
		operation2type.put("iput-char", 1);
		operation2type.put("iput-short", 1);

		operation2type.put("sget", 0);
		operation2type.put("sget-wide", 0);
		operation2type.put("sget-object", 0);
		operation2type.put("sget-boolean", 0);
		operation2type.put("sget-byte", 0);
		operation2type.put("sget-char", 0);
		operation2type.put("sget-short", 0);

		operation2type.put("sput", 1);
		operation2type.put("sput-wide", 1);
		operation2type.put("sput-object", 1);
		operation2type.put("sput-boolean", 1);
		operation2type.put("sput-byte", 1);
		operation2type.put("sput-char", 1);
		operation2type.put("sput-short", 1);
		
		operation2type.put("invoke-", 2);
	}
	
	public int getType(){
		return type;
	}
	
	public String getVar(){
		return varStr;
	}
	
	public String getVarType(){
		return varType;
	}
	
	public String getOperation(){
		return operation;
	}
	
	public static String parseVarFromInit(String classStr, StringBuffer cmd){
		String cmdStr = cmd.toString();
		if(cmdStr.indexOf(classStr + ";->") > 0){
			String varName, varType;
			
			int bpos_vname = cmdStr.indexOf(";->") + 3;
			int epos_vname = cmdStr.indexOf(" ", bpos_vname);
			if(epos_vname <= bpos_vname)
				varName = "unknown";
			else
				varName = cmdStr.substring(bpos_vname, epos_vname);
			
			//int bpos_vtype = cmdStr.lastIndexOf("/") + 1;
			int bpos_vtype = epos_vname + 1;
			int epos_vtype = cmdStr.lastIndexOf(";");
			if(epos_vtype <= bpos_vtype)
				varType = "unknown";
			else
				varType = cmdStr.substring(bpos_vtype, epos_vtype);
			
			return varType + " " + varName;
		}
		return "unknown unknown";
	}

	public String toJson(){
		StringBuffer r = new StringBuffer("");
		r.append("{\"id\":\"" + classID + "->" + varStr + " " + varType + "\",\"op\":\"" + operation + "\"},");
		return r.toString();
	}
	
}
