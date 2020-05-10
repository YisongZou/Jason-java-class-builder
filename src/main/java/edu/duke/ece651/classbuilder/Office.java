package edu.duke.ece651.classbuilder;
import java.util.*;
import org.json.*;
import java.io.*;
public class Office{
private Faculty occupant;
public Faculty getOccupant(){
return occupant;
}
public void setOccupant(Faculty occupant){
this.occupant=occupant;
}
private int number;
public int getNumber(){
return number;
}
public void setNumber(int number){
this.number=number;
}
private String building;
public String getBuilding(){
return building;
}
public void setBuilding(String building){
this.building=building;
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
ans.put("type","Office");
unique_ids.put(this, unique_ids.size() + 1 );//object
JSONObject occupantobj = new JSONObject();
if(unique_ids.get(occupant) == null){
occupantobj.put("occupant",occupant.jsonhelper(unique_ids));
values.put(occupantobj);
}
else{
occupantobj.put("ref",unique_ids.get(occupant));
values.put(occupantobj);
}
//normal
JSONObject numberobj = new JSONObject();
numberobj.put("number",number);
values.put(numberobj);
//normal
JSONObject buildingobj = new JSONObject();
buildingobj.put("building",building);
values.put(buildingobj);
ans.put("values",values);
return ans;
}
}
