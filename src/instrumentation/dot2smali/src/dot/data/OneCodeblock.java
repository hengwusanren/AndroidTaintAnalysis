package dot.data;

import java.util.ArrayList;
import java.util.HashMap;

public class OneCodeblock {

	private String codeblockID;
	private String color;
	private String label;
	private ArrayList<StringBuffer> cmds;
	private ArrayList<VarOpItem> varOperations;
	
	public OneCodeblock(String lineStr, String className, String methodName, HashMap<String, String> varList, ArrayList<String> methodList){
		//int bpos_ID = lineStr.indexOf("\"") + 1;
		int bpos_ID = lineStr.indexOf("-BB@");
		int epos_ID = lineStr.indexOf("\" [color=\"", bpos_ID);
		if(bpos_ID < 1 || epos_ID <= bpos_ID)
			codeblockID = className + "->unknown";
		else
			codeblockID = methodName + lineStr.substring(bpos_ID, epos_ID);

		int bpos_Color = epos_ID + 10;
		int epos_Color = lineStr.indexOf("\", label=\"", bpos_Color + 1);
		if(bpos_Color < 10 || epos_Color <= bpos_Color)
			color = "unknown";
		else
			color = lineStr.substring(bpos_Color, epos_Color);

		int bpos_Label = epos_Color + 10;
		int epos_Label = lineStr.indexOf("\"]", bpos_Label + 1);
		if(bpos_Label < 10 || epos_Label <= bpos_Label)
			label = "unknown";
		else
			label = lineStr.substring(bpos_Label, epos_Label);
		
		parseCmds(label);
		getVarOps(cmds, className, varList, methodList);
	}
	
	private void parseCmds(String labelStr){
		cmds = new ArrayList<StringBuffer>();
		int bpos = 0;
		int epos = labelStr.indexOf("\\l", bpos);
		while(epos > 0){
			cmds.add(new StringBuffer(labelStr.substring(bpos, epos)));
			bpos = epos + 2;
			epos = labelStr.indexOf("\\l", bpos);
		}
	}
	
	private void getVarOps(ArrayList<StringBuffer> cmdStrs, String className, HashMap<String, String> varList, ArrayList<String> methodList){
		varOperations = new ArrayList<VarOpItem>();
		//System.out.println("fuck cmdstrs: " + cmdStrs.size());//<--
		for(StringBuffer sb : cmdStrs){
			VarOpItem voi = new VarOpItem(sb, className, varList, methodList);
			if(voi.getType() != -1)
				varOperations.add(voi);
		}
	}
	
	public String getID(){
		if(codeblockID.equals(null))
			return "unknown";
		
		return codeblockID;
	}
	
	public String getColor(){
		if(color.equals(null))
			return "unknown";
		
		return color;
	}
	
	public String getLabel(){
		if(label.equals(null))
			return "unknown";
		
		return label;
	}
	
	public ArrayList<StringBuffer> getCmds(){
		return cmds;
	}
	
	public String[] getCommands(){
		String[] lines = new String[cmds.size()];
		for(int i = 0; i < cmds.size(); i++){
			lines[i] = cmds.get(i).toString();
		}
		return lines;
	}
	
	@Override
	public String toString(){
		StringBuffer varOps = new StringBuffer("");
		for(VarOpItem vo : varOperations){
			varOps.append("#" + codeblockID + "\\l(" + vo.getVarType() + ")" + vo.getVar() + " " + vo.getOperation() + "\\l");
		}
		return "\"" + codeblockID + "\" [color=\"" + color + "\", label=\"" + varOps + "\"]";
	}
	
	public String toJson(){
		int n = varOperations.size();
		if(n <= 0) {
			//System.out.println("NO VAR OPS!");
			return "\"\"";
		}
		StringBuffer r = new StringBuffer("[");
		for(int i = 0; i < n; i++){
			r.append(varOperations.get(i).toJson());
		}
		r.deleteCharAt(r.length() - 1);
		r.append("]");
		return r.toString();
	}
	
}
