package dot.data;

public class OneEdge {

	private String fromCodeblockID;
	private String toCodeblockID;
	private String color;
	
	public OneEdge(String lineStr){
		if(!aLineIsOneEdge(lineStr)){
			fromCodeblockID = "unknown";
			toCodeblockID = "unknown";
			color = "unknown";
			return;
		}
		
		int bpos_From = lineStr.indexOf("\"") + 1;
		int epos_From = lineStr.indexOf("\" -> \"", bpos_From);
		if(bpos_From < 1 || epos_From <= bpos_From)
			fromCodeblockID = "unknown";
		else
			fromCodeblockID = lineStr.substring(bpos_From, epos_From);
		
		int bpos_To = epos_From + 6;
		int epos_To = lineStr.indexOf("\" [color=\"", bpos_To);
		if(bpos_To < 6 || epos_To <= bpos_To)
			toCodeblockID = "unknown";
		else
			toCodeblockID = lineStr.substring(bpos_To, epos_To);
		
		int bpos_Color = epos_To + 10;
		int epos_Color = lineStr.indexOf("\"];", bpos_Color);
		if(bpos_Color < 10 || epos_Color <= bpos_Color)
			color = "unknown";
		else
			color = lineStr.substring(bpos_Color, epos_Color);
		
	}
	
	public String getFromCodeblock(){
		if(fromCodeblockID.equals(null))
			return "unknown";
		
		return fromCodeblockID;
	}
	
	public String getToCodeblock(){
		if(toCodeblockID.equals(null))
			return "unknown";
		
		return toCodeblockID;
	}
	
	public String getColor(){
		if(color.equals(null))
			return "unknown";
		
		return color;
	}
	
	public static boolean aLineIsOneEdge(String lineStr){
		if(lineStr.indexOf("\" -> \"") < 0 || lineStr.indexOf("\"];") < 0)
			return false;
		return true;
	}
	
	@Override
	public String toString(){
		String e = "\"" + fromCodeblockID + "\" -> \"" + toCodeblockID + "\" [color=\"" + color + "\"];";
		//System.out.println(e);
		return e;
	}
}
