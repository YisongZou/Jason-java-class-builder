package edu.duke.ece651.classbuilder;
import java.util.*;
import org.json.*;
import java.io.*;
public class Faculty{
private String name;
public String getName(){
return name;
}
public void setName(String name){
this.name=name;
}
private ArrayList<Course> taught;
public int numTaught(){
return taught.size();
}
public void addTaught(Course n){
taught.add(n);
}
public Course getTaught(int index){
return taught.get(index);
}
public void setTaught(int index, Course n){
taught.set(index, n);
}
public Faculty(){
taught = new ArrayList<Course>();
}
public JSONObject toJSON()throws JSONException{
JSONObject ans = new JSONObject();
ans = jsonhelper(new HashMap<Object, Integer>());
return ans;
}
public JSONObject jsonhelper(HashMap<Object, Integer> unique_ids) {
JSONObject ans = new JSONObject();
JSONArray values = new JSONArray();
int code = unique_ids.size() + 1;
ans.put("id",code);
ans.put("type","Faculty");
unique_ids.put(this, unique_ids.size() + 1 );//normal
JSONObject nameobj = new JSONObject();
nameobj.put("name",name);
values.put(nameobj);
//array of class
JSONObject taughtobj = new JSONObject();
JSONArray taughtarray = new JSONArray();
for (Course temp : taught) {
if(unique_ids.get(temp) == null){
taughtobj.put("taught",temp.jsonhelper(unique_ids));
taughtarray.put(taughtobj);
}
else{
taughtobj.put("ref",unique_ids.get(temp));
taughtarray.put(taughtobj);
}
}
values.put(taughtarray);
ans.put("values",values);
return ans;
}
}
