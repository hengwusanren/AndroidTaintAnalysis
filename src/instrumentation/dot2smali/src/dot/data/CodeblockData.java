package dot.data;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/*
 * 代码块类
 * 代码块ID、指向其他代码块的有向边、代码内容
 */
public class CodeblockData {

	private String codeblockID;
	private ArrayList<OneEdge> edges;
	private OneCodeblock context;
	
	public CodeblockData(ArrayList<String> codeblockLines, String className, String methodName, HashMap<String, String> varList, ArrayList<String> methodList){
		edges = new ArrayList<OneEdge>();
		for(String lineTxt : codeblockLines){
			if(OneEdge.aLineIsOneEdge(lineTxt)){
				OneEdge e = new OneEdge(lineTxt);
				System.out.println("[log] new OneEdge: " + e.toString());
				edges.add(e);
			}
			else{
				System.out.println("[log] new OneCBContext: " + lineTxt);
				context = new OneCodeblock(lineTxt, className, methodName, varList, methodList);//一个代码块可以有几条边，但反正只会有一行是真正的代码内容
				//System.out.println("fuck: ");
			}
		}
		codeblockID = context.getID();
		
		//System.out.println(this.getEdgesCount());
	}
	
	public String getID(){
		if(codeblockID.equals(null))
			return "unknown";
		
		return codeblockID;
	}
	
	public int getEdgesCount(){
		return edges.size();
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
	
	public String[] getCommands(){
		return context.getCommands();
	}
	
	@Override
	public String toString(){
		StringBuffer r = new StringBuffer("");
		for(OneEdge e : edges){
			System.out.println(e.toString());
			r.append(e.toString() + "\n");
		}
		r.append(context.toString() + "\n");
		return r.toString();
	}
	
	public String toJson(){
		String c = context.toJson();
		if(c.equals("\"\"")) return "";
		StringBuffer r = new StringBuffer("");
		r.append("{\"id\":\"" + codeblockID + "\",\"varops\":");
		r.append(c);
		r.append("},");
		return r.toString();
	}
	
	public void outPut(FileWriter fw){
		//将代码块写入文件
		try {
			fw.write(this.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
