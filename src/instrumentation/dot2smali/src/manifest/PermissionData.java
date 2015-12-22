package manifest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import dot.data.MethodData;

public class PermissionData {
	public PermissionData() {}
	public PermissionData(String filePath, String permission) {
		// permission is like: "WRITE_EXTERNAL_STORAGE"
		
		try {
            File fr = new File(filePath);
    		
            if(fr.isFile() && fr.exists()){
            	
            	String encoding = "UTF-8";
	    		String newFilePath = filePath.substring(0, filePath.indexOf(".xml")) + "[new].xml";
            	
                InputStreamReader reader = new InputStreamReader(new FileInputStream(fr), encoding);
                BufferedReader bufferedReader = new BufferedReader(reader);
    			FileWriter writer = new FileWriter(newFilePath, true);
    			
    			String newPermission = "<user-permission android:name=\"android.permission." + permission + "\" />";
    			boolean flag = false;
                String line = null;
                while((line = bufferedReader.readLine()) != null){
                	String lineStr = line.trim();
                	
                	if(lineStr.indexOf(permission) >= 0) flag = true;
                	
                	if(!flag && lineStr.indexOf("<user-permission ") >= 0){
                		writer.write(line + "\n");
                		writer.write(newPermission + "\n");
                		flag = true;
                		continue;
                	}
                	
                	writer.write(line + "\n");
                }
                
                reader.close();
                writer.close();
                
                fr.delete();
                fr = new File(newFilePath);
                fr.renameTo(new File(filePath));
	        }
            else{
            	System.out.println("No such a file: " + filePath);
	        }
        }
        catch(IOException e){
            e.printStackTrace();
        }
	}
}
