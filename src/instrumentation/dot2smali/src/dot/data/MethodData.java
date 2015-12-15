package dot.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.*;

public class MethodData {
	
	private String dotFilePath;//dot文件路径
	private String className;
	private String methodID;
	private ArrayList<CodeblockData> codeblocks;
	
	public MethodData(){}
	
	public MethodData(String classid, String fileName, HashMap<String, String> varList, ArrayList<String> methodList){
		File f = new File(fileName);
		dotFilePath = f.getAbsolutePath();
		//className = f.getParentFile().getName();
		className = classid;
		String[] methodInfo = f.getName().split("-");
		//if(methodInfo.length != 3)
		if(true)
			methodID = classid + "->" + f.getName().substring(0, f.getName().indexOf(".dot"));//modified in 20140709
			//methodID = f.getName().substring(0, f.getName().indexOf(".dot"));
		else{
			methodID = classid + "->" + methodInfo[0];
			//methodID = methodInfo[0];
			
			//methodType = methodInfo[1];
			//methodKind = methodInfo[2];
		}
		codeblocks = new ArrayList<CodeblockData>();
		readDotFile(fileName, varList, methodList);
		generateNewDOT();
		
		//codeblocks默认排好序了，BB的ID增序。
	}
	
	private void readDotFile(String fileName, HashMap<String, String> varList, ArrayList<String> methodList){
        try {
            String encoding = "UTF-8";
            File file = new File(fileName);
            if(file.isFile() && file.exists()){
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                ArrayList<String> codeblockLines = new ArrayList<String>();
                while((lineTxt = bufferedReader.readLine()) != null){
                    codeblockLines.add(lineTxt);
                	if(lineTxt.indexOf("\", label=\"") > 0){
                		System.out.println("[log] lineTxt: " + lineTxt);
                		CodeblockData cbdata = new CodeblockData(codeblockLines, className, methodID, varList, methodList);
                		System.out.println("[log] edgeCount: " + cbdata.getEdgesCount());
                    	codeblocks.add(cbdata);
                    	codeblockLines.clear();
                    }
                }
                read.close();
	        }
            else{
	            System.out.println("no such a file: " + fileName);
	        }
        }
        catch(Exception e){
            System.out.println("failed to read: " + fileName);
            e.printStackTrace();
        }
    }
	
	public String getID(){
		if(methodID.equals(null))
			return "unknown";
		
		return methodID;
	}
	
	public ArrayList<String> getVarsUsed(){
		ArrayList<String> varsUsed = new ArrayList<String>();
		
		return varsUsed;
	}
	
	public ArrayList<String> getVarsWritten(){
		ArrayList<String> varsWritten = new ArrayList<String>();
		
		return varsWritten;
	}
	
	public ArrayList<String> getVarsRead(){
		ArrayList<String> varsRead = new ArrayList<String>();
		
		return varsRead;
	}
	
	public ArrayList<String> getCodeblocksIDs(){
		ArrayList<String> codeblocksIDs = new ArrayList<String>();
		
		return codeblocksIDs;
	}
	public CodeblockData[] getCodeblocks(){
		return codeblocks.toArray(new CodeblockData[0]);
	}
	
	//生成该method的新dot文件，其实就是把所有codeblocks的context都替换成各自的VarOperations
	private void generateNewDOT(){
		String newDotName = dotFilePath.substring(0, dotFilePath.indexOf(".dot")) + "[new].dot";
		try {
			FileWriter writer = new FileWriter(newDotName, true);
			System.out.println(codeblocks.size());
			//将codeblocks中所有codeblock写入文件中
			for(CodeblockData cbdata : codeblocks){
				//cbdata.outPut(writer);
				System.out.println(cbdata.getEdgesCount());
				writer.write(cbdata.toString());
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//这里只是为了连缀各个CB，不是严格的json
	public String toJson(){
		StringBuffer r = new StringBuffer("");
		for(CodeblockData cbdt : codeblocks){
			r.append(cbdt.toJson());
		}
		return r.toString();
	}
}
