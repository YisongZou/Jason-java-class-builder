package edu.duke.ece651.classbuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

//Building the classes
public class ClassBuilder {
  private JSONObject my_jsonobject; 
  //Constructor for string
  public ClassBuilder(String str){
    this.my_jsonobject = new JSONObject(str);
     System.out.println(my_jsonobject);
}
  //Constructor for Inputstream
  public ClassBuilder(InputStream input){
    String result = new BufferedReader(new InputStreamReader(input)).lines().collect(Collectors.joining(System.lineSeparator()));
    this.my_jsonobject = new JSONObject(result);
     System.out.println(my_jsonobject);
}

  //Get the class names in JSON file
  public Collection<String> getClassNames(){
    Collection<String> ans = new ArrayList<String>();
    JSONArray jsonClasses = this.my_jsonobject.getJSONArray("classes");
    for(int i = 0 ; i < jsonClasses.length(); i++){
     JSONObject classes = jsonClasses.getJSONObject(i);
     String name = classes.getString("name");
    ans.add(name);
  }
  return ans;
}

  //Decide if a field is itself a normal type
  public boolean is_class(String s){
    if(s.equals("boolean")||s.equals("byte")||s.equals("char")||s.equals("short")||s.equals("int")||s.equals("long")||s.equals("float")||s.equals("double")||s.equals("String")){
      return false;
    }
    return true;
  }

  //Get the source code of json and return it as a string
public String getSourceCode(String className){
    String ans = new String();
    String tojson = new String();
    //tojson is the string to store the code for json file
    tojson += "public JSONObject toJSON()throws JSONException{\n";
    tojson += "JSONObject ans = new JSONObject();\n";
    tojson += "ans = jsonhelper(new HashMap<Object, Integer>());\nreturn ans;\n}\n";
    tojson += "public JSONObject jsonhelper(HashMap<Object, Integer> unique_ids) {\n";
    tojson += "JSONObject ans = new JSONObject();\nJSONArray values = new JSONArray();\n";
    tojson += "int code = unique_ids.size() + 1;\n";
    tojson = tojson + "ans.put(\"id\",code);\nans.put(\"type\"," +"\""+ className+"\"" + ");\n";
    tojson = tojson + "unique_ids.put(this, unique_ids.size() + 1 );";
    JSONArray jsonClasses = this.my_jsonobject.getJSONArray("classes");
    for(int i = 0 ; i < jsonClasses.length(); i++){
      JSONObject classes = jsonClasses.getJSONObject(i);
     String name = classes.getString("name");
     if (name == className) {
       if (null != my_jsonobject.optString("package") ) {
         //ans is the string to store the code for classes
         ans += "package ";
         ans += my_jsonobject.optString("package");
         ans += ";\n";
  }
       ans += "import java.util.*;\n";
       ans += "import org.json.*;\n";
       ans += "import java.io.*;\n";
       ans += "public class ";
       ans = ans + name + "{\n";
       JSONArray fields = classes.getJSONArray("fields");
       for (int j = 0; j < fields.length(); j++) {
         JSONObject field = fields.getJSONObject(j);
         String field_name = field.getString("name");
         String up = field_name.substring(0, 1).toUpperCase() + field_name.substring(1);
         if (null == field.optJSONObject("type")) {
           //Not an array
           String array = field.getString("type");
           ans = ans + "private " + array + " " + field_name + ";\n";
           ans = ans + "public " + array + " get" + up + "(){\n" + "return " + field_name + ";\n}\n";
           ans = ans + "public void set" + up + "(" + array + " " + field_name + "){\n" + "this." + field_name + "="
               + field_name + ";\n}\n";
               if (is_class(array)) {
                 //is normla type
                 tojson += "//object\nJSONObject " + field_name + "obj"
                     + " = new JSONObject();\n";
                     tojson = tojson + "if(unique_ids.get(" + field_name  + ") == null){\n";
                 tojson = tojson + field_name + "obj.put(" + "\"" + field_name + "\"," + field_name
                     + ".jsonhelper(unique_ids));\nvalues.put(" + field_name
                             + "obj);\n}\nelse{\n" + field_name +  "obj.put(\"ref\",unique_ids.get(" + field_name 
                   + "));\n" + "values.put(" + field_name + "obj);\n}\n";
               }
               else{
                 //is a class
                 tojson = tojson + "//normal\nJSONObject " + field_name + "obj" + " = new JSONObject();\n";
                 tojson = tojson + field_name + "obj" + ".put(" + "\"" + field_name + "\"" + "," + field_name + ");\n";
                 tojson = tojson + "values.put(" + field_name + "obj);\n";
               }
         } else{
           //Is an array
           JSONObject current_obj = field.getJSONObject("type");
             String array_type = current_obj.getString("e");
             ans = ans + "private " + "ArrayList<"+ array_type + "> " + field_name + ";\n";
              ans = ans + "public int num" + up + "(){\n" + "return " + field_name +".size()" + ";\n}\n";
              ans = ans + "public void add" + up + "(" + array_type + " n" +  "){\n"  + field_name +".add(n)" + ";\n}\n";
              ans = ans + "public " + array_type +" get" + up + "(int index)"+ "{\n"  + "return " + field_name +".get(index)" + ";\n}\n";
              ans = ans + "public void " +"set" + up + "(int index, "+ array_type +  " n){\n"  + field_name +".set(index, n)" + ";\n}\n";
              ans = ans + "public " + name + "(){\n" + field_name + " = new ArrayList<" + array_type + ">();\n}\n";
               if (is_class(array_type)) {
                 //is an array of classes
                  tojson = tojson + "//array of class\nJSONObject " + field_name + "obj" + " = new JSONObject();\n"; 
                  tojson = tojson + "JSONArray " + field_name + "array" + " = new JSONArray();\n";
                  tojson = tojson + "for (" + array_type + " temp : " + field_name + ") {\n";
                  tojson = tojson + "if(unique_ids.get(" +"temp"+ ") == null){\n";
                  tojson = tojson + field_name + "obj.put("  +"\""+ field_name+ "\","+ "temp"
                      + ".jsonhelper(unique_ids));\n";
                      tojson = tojson + field_name + "array" + ".put(" + field_name + "obj);\n}\nelse{\n";
                      tojson = tojson +  field_name +  "obj.put(\"ref\",unique_ids.get(" + "temp"         
                   + "));\n" + field_name + "array"   +".put(" + field_name + "obj);\n}\n}\n";
                      tojson = tojson +  "values.put(" + field_name + "array);\n";
               }
               else{
                 //is an array of normal classes
                 tojson = tojson + "//array of normal type\nJSONObject " + field_name + "obj" + " = new JSONObject();\n";
                 tojson = tojson + "JSONArray " + field_name + "array" + " = new JSONArray();\n";
                 tojson = tojson + "for (" + array_type + " temp : " + field_name + ") {\n";
                 tojson = tojson + field_name + "array" + ".put(toString(temp));\n}\n";
                 tojson = tojson + field_name + "obj.put(" + "\"" +  field_name + "\"," +field_name +  "array);\n";
                 tojson = tojson +  "values.put(" + field_name + "array);\n"; 
               }
         }
       }
       tojson += "ans.put(\"values\",values);\nreturn ans;\n}\n"; 
       ans += tojson;
       ans += "}\n";
     }
  }
  return ans;
}

public void createAllClasses(String basePath) {
  String package_path = "";

  //get path
  if (null == my_jsonobject.optString("package") ) {
      System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
  }
  else{
package_path = my_jsonobject.optString("package");
      package_path = package_path.replace(".", "/");
      System.out.println(".......................................");
  }
  if (!basePath.endsWith("/")) {
    basePath += "/";
  }
  if(package_path!=""){
    package_path += "/";
  }
  basePath += package_path;
    JSONArray jsonClasses = this.my_jsonobject.getJSONArray("classes");
    for(int i = 0 ; i < jsonClasses.length(); i++){
     JSONObject classes = jsonClasses.getJSONObject(i);
     String name = classes.getString("name");
     String output = getSourceCode(name);
     name+=".java";
     //No such file path
     File folder = new File(basePath);
        if (!folder.exists() && !folder.isDirectory()) {
            System.out.println("There is no such path, creating path:" + basePath);
            folder.mkdirs();
        } else {
            System.out.println("There is such path:" + basePath);
        }
        // No such file
        File file = new File(basePath + name);
        if (!file.exists()) {
            System.out.println("There is no such file, creating file:" + basePath + name);
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("There is already such file:" + basePath + name);
        }
       String Fullpath = basePath + name;
       try{
         FileWriter fileWritter = new FileWriter(Fullpath,true);
         fileWritter.write(output);
         fileWritter.close();
       }
       catch (IOException e) {
                e.printStackTrace();
            }
    }

    String deserial = new String();
     if (null != my_jsonobject.optString("package") ) {                                                                                                                                                                          
         deserial += "package ";                                                                                                                                                                                                           deserial += my_jsonobject.optString("package");                                                                                                                                                                                   deserial += ";\n";                                                                                                                                                                                                              }                                                                                                                                                                                                                                    deserial += "import java.util.*;\n";                                                                                                                                                                                              deserial += "import org.json.*;\n";                                                                                                                                                                                               deserial += "import java.io.*;\n";
     deserial = deserial + "public class Deserializer {\n";
    /////////////////////////////////////////////////////////////////////////////
    //Deserilaizer
      for(int i = 0 ; i < jsonClasses.length(); i++){
     JSONObject classes = jsonClasses.getJSONObject(i);
     String name = classes.getString("name");
       String up = name.substring(0, 1).toUpperCase() + name.substring(1);
         deserial = deserial + "public static " + up + " read" + up + "(JSONObject js) throws JSONException{\n"; 
         deserial = deserial + up + " " + up + "obj" + " = new " + up + "();\n";
         deserial += "JSONArray fields = js.getJSONArray(\"values\");\n";
         deserial += "for (int i=0;i<fields.length();i++){\n";
         deserial += "JSONObject temp = fields.getJSONObject(i);\nIterator<String> it = temp.keys();\nwhile(it.hasNext()){\nString key = it.next();\nString value = temp.getString(key);\n";
         deserial = deserial + up + "obj.key=value;\n}\n}\n}\n";
      }
      deserial += "}\n";
     //No such file path
     File folder = new File(basePath);
        if (!folder.exists() && !folder.isDirectory()) {
            System.out.println("There is no such path, creating path:" + basePath);
            folder.mkdirs();
        } else {
            System.out.println("There is such path:" + basePath);
        }
        // No such file
        File file = new File(basePath + "Deserializer.java");
        if (!file.exists()) {
            System.out.println("There is no such file, creating file:" + basePath + "Deserializer.java");
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("There is already such file:" + basePath + "Deserializer.java");
        }
       String Fullpath = basePath + "Deserializer.java";
       try{
         FileWriter fileWritter = new FileWriter(Fullpath,true);
         fileWritter.write(deserial);
         fileWritter.close();
       }
       catch (IOException e) {
                e.printStackTrace();
            }
    }
  
}
