package edu.duke.ece651.classbuilder;

import java.io.*;

public class ReadJson {
  public static String read_json(String file) throws IOException {
		//Read Data From File
    System.out.println("Reading File");
		StringBuffer strbuffer = new StringBuffer();
		File myFile = new File(file);//file:The path of json file
		if (!myFile.exists()) {
			System.err.println("Can't Find " + file);
		}
		try {
			FileInputStream input = new FileInputStream(file);
			InputStreamReader inputStreamReader = new InputStreamReader(input);
			BufferedReader in  = new BufferedReader(inputStreamReader);
      //Using str to store
			String str;
			while ((str = in.readLine()) != null) {
				strbuffer.append(str);  
			}
			in.close();
		} catch (IOException e) {
			e.getStackTrace();
		}
    //Finish reading and store to string
		return strbuffer.toString();
	}
}
