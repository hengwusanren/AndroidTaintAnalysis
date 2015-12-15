package json;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Classes2Json {
	
	public Classes2Json(){}
	
	public Classes2Json(String fileName, ArrayList<dot.data.ClassData> classesData){
		
		StringBuffer r = new StringBuffer("[");
		
		for(dot.data.ClassData classdt : classesData){
			//if(classdt.getMethodNames().length > 0)
			//System.out.println("fuck: " + classdt.getID());
			r.append(classdt.toJson());
			//System.out.println("fuck: " + classdt.toJson());
		}
		
		r.deleteCharAt(r.length() - 1);
		r.append("]");
		
		try {
			FileWriter writer = new FileWriter(fileName, false);
			System.out.println(r.toString());
			writer.write(r.toString());
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
