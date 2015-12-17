package smali.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import dot.data.*;

public class ClassData {

	private String inputSmaliPath;
	private String classID;
	private String dirPath;
	private String[] vars;
	private String[] methods;
	private boolean debug = false;
	
	public ClassData(String fileName, dot.data.ClassData classdt){
		if(classdt == null){
			if(debug) System.out.println("[LOG] no classdata found!");
			return;
		}
		
		classID = classdt.getID();
		if(debug) System.out.println("classID: " + classID);
		inputSmaliPath = fileName;
		vars = classdt.getVarNames();
		methods = classdt.getMethodNames();
		
		findCodeBlocks(classdt);
	}
	
	/*
	 * 扫描一个smali文件时，根据其classID从dot文件中得到对应classdata；
	 * 遇到# instance fields，则认为是<init>-BB@0x0代码块；
	 * 遇到.method p时，得到其methodID，得到classdata中对应methoddata，从前往后，顺序对应methoddata中codeblock的ID
	 */
	public void findCodeBlocks(dot.data.ClassData classdt){		
		try {
            File fr = new File(inputSmaliPath);
    		
            if(fr.isFile() && fr.exists()){
            	
            	String encoding = "UTF-8";
	    		String newSmaliName = inputSmaliPath.substring(0, inputSmaliPath.indexOf(".smali")) + "[new].smali";
            	//String newSmaliName = inputSmaliPath.substring(0, inputSmaliPath.indexOf(".smali")) + ".smali";
	    		
	    		MethodData curMethoddt = new MethodData();
	    		int codeblockIndex = 0;
	    		int codeblockLineIndex = 0;
	    		int writeState = 0;//1表示正常在method体内
	    		int dotParas = 0;//how many parameters the current method has. Important!
	    		int dotLocals = 0;//how many non-parameter registers the current method will use. Important!
	    		int registerTooMany = 0;//if the number of registers is larger than 16
	    		ArrayList<String> codeblockLines = new ArrayList<String>();
	    		
            	
                InputStreamReader reader = new InputStreamReader(new FileInputStream(fr), encoding);
                BufferedReader bufferedReader = new BufferedReader(reader);
    			FileWriter writer = new FileWriter(newSmaliName, true);
    			
                String line = null;
                while((line = bufferedReader.readLine()) != null){
                	String lineStr = line.trim();
                	
                	//packed-switch属于代码块中可能出现的dalvik指令，前面带有'.'，其他类似情况还有待枚举！
                	if(lineStr.equals("") || lineStr.charAt(0) == ':' || 
                			(lineStr.charAt(0) == '.' && lineStr.indexOf("method") != 1 && !lineStr.equals(".end method") &&
                					(lineStr.indexOf(".locals ") != 0) && lineStr.indexOf("packed-switch") != 1)){
                		writer.write(line + "\n");
                		continue;
                	}
                	
                	//<init>
                	if(lineStr.indexOf("# instance fields") >= 0){
                		writer.write(line + "\n");
                		//writer.write(insertLogCB("<init>-BB@0x0", true));//init不需要log输出代码块信息！
                		continue;
                	}
                	
                	//.method
                	if(lineStr.indexOf(".method ") == 0){
                		if(debug) System.out.print("\n.method ");
                		String methodnm = containMethod(lineStr, classdt.getMethodNames());
                		if(debug) System.out.println("methodnm: ");
                		if(!methodnm.equals("")){
                			dotParas = countMethodParas(lineStr); // parse the number of parameters this method requires
                			if(debug) System.out.print(methodnm + "\n");
                			curMethoddt = classdt.getMethodDataByName(methodnm);
                			codeblockIndex = 0;
                			codeblockLineIndex = 0;
                			if(curMethoddt.getCodeblocks().length > 0)
                				writeState = 1;
                		}
                		writer.write(line + "\n");
                		continue;
                	}
                	
                	//.end method
                	if(lineStr.equals(".end method")){
                		writeState = 0;
                		registerTooMany = 0;
                		writer.write(line + "\n");
                		continue;
                	}
                	
                	//.registers
                	if(lineStr.indexOf(".locals ") == 0 && writeState == 1){
            			dotLocals = Integer.parseInt(lineStr.substring(8)) + 2;
            			if(dotLocals + dotParas >= 16){ // sometimes "this" is an implicit parameter
            				registerTooMany = 1;
            				writer.write(line + "\n");
            			}
            			else{
            				registerTooMany = 0;
            				writer.write(line.substring(0, line.indexOf(".locals ") + 8) + Integer.toString(dotLocals) + "\n");
            			}
            			continue;
            		}
                	
                	//现在的比对算法还存在很大问题！不够鲁棒，默认为代码块的指令谓词完全相等
                	//在method体内，且该行开头的指令与当前代码块的当前行指令相同
                	if(writeState == 1 
                			&& registerTooMany == 0 
                			&& !lineStr.equals("") 
                			&& (curMethoddt.getCodeblocks()[codeblockIndex].getCommands()[codeblockLineIndex].indexOf(lineStr) >= 0 
                				|| (lineStr.indexOf(" ") >= 0 
                					&& curMethoddt.getCodeblocks()[codeblockIndex].getCommands()[codeblockLineIndex].indexOf(lineStr.substring(1, lineStr.indexOf(" ") + 1)) >= 0
                					)
                				)
                			){
                		if(debug) System.out.println(curMethoddt.getCodeblocks()[codeblockIndex].getID());
                		//codeblockLines.add(line);
                		codeblockLineIndex++;
                		if(codeblockLineIndex == 1){// onCreate不需要log输出代码块信息！
                			writer.write(insertLogCB(curMethoddt.getCodeblocks()[codeblockIndex].getID(), false, dotLocals));
                		}
                		if(codeblockLineIndex == curMethoddt.getCodeblocks()[codeblockIndex].getCommands().length){
                			//将代码块ID及内容写入？
                			                			
                			codeblockLineIndex = 0;
                			//codeblockLines = new ArrayList<String>();
                			codeblockIndex++;
                			
                			//20140710
                			if(codeblockIndex == curMethoddt.getCodeblocks().length) {
                				codeblockIndex = 0;
                				writeState = 0;
                			}
                		}
//                		if(lineStr.indexOf(".registers ") == 0){
//                			dotLocals = Integer.parseInt(lineStr.substring(11)) + 2;
//                			writer.write(line.substring(0, line.indexOf(".registers ") + 11) + Integer.toString(dotLocals) + "\n");
//                		}
//                		else
                		writer.write(line + "\n");
                		continue;
                	}
                	
                	writer.write(line + "\n");
                }
                reader.close();
                writer.close();
                
                fr.delete();
                fr = new File(newSmaliName);
                fr.renameTo(new File(inputSmaliPath));
	        }
            else{
            	if(debug) System.out.println("No such a file: " + inputSmaliPath);
	        }
        }
        catch(IOException e){
        	if(debug) System.out.println("Failed to read or write.");
            e.printStackTrace();
        }
	}
	
	private int countMethodParas(String smlLine){
		// todo: count parameters of a method given its smali-style declaration
		
		// get string in "()"
		int pos0 = smlLine.indexOf('(');
		if(pos0 < 0) return 0;
		int pos1 = smlLine.indexOf(')', pos0);
		if(pos1 < 0) return 0;
		String paras = smlLine.substring(pos0 + 1, pos1);
		
		return countTypesFromString(paras);
	}
	
	private static int countTypesFromString(String s){
		// a type is like "Z", "B", "S", "C", "I", "J", "F", "D", "L...;", "[...".
        if(s.length() == 0) return 0;
        char head = s.charAt(0);

        if(head == '[') return countTypesFromString(s.substring(1));

        String basicTypes = "ZBSCIJFD";
        if(basicTypes.indexOf(head) >= 0) return 1 + countTypesFromString(s.substring(1));

        if(head == 'L') {
            int pos = s.indexOf(';');
            if(pos < 0) return 0;
            return 1 + countTypesFromString(s.substring(pos + 1));
        }

        return 1;
	}
	
	private String containMethod(String smlLine, String[] methods){
		for(String s : methods){
			//System.out.println(s);
			if((classID + "->" + smlLine.substring(smlLine.lastIndexOf(" ") + 1).replace('(', '-').replace(')', '-').replace('/', '_').replaceAll(";", "")).
					indexOf(s) == 0) {
				if(debug) System.out.println(s);
				return s;
			}
		}
		return "";
	}
	
	public String insertLogCB(String cbID, boolean isfield, int regNum){
		if(isfield){
			return "\nconst-string v" + Integer.toString(regNum - 2) + ", \"code_block\"\n" + "const-string v" + Integer.toString(regNum - 1) + ", \"" + cbID + "\"\n" +
				"invoke-static {v" + Integer.toString(regNum - 2) + ", v" + Integer.toString(regNum - 1) + "}, Landroid/util/Log;->v(Ljava/lang/String;Ljava/lang/String;)I\n\n";
		}
		return "\n    const-string v" + Integer.toString(regNum - 2) + ", \"code_block\"\n    " + "const-string v" + Integer.toString(regNum - 1) + ", \"" + cbID + "\"\n    " +
			"invoke-static {v" + Integer.toString(regNum - 2) + ", v" + Integer.toString(regNum - 1) + "}, Landroid/util/Log;->v(Ljava/lang/String;Ljava/lang/String;)I\n\n";
	}
}
