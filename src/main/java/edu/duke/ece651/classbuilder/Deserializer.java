package edu.duke.ece651.classbuilder;
import java.util.*;
import org.json.*;
import java.io.*;
public class Deserializer {
public static Course readCourse(JSONObject js) throws JSONException{
Course Courseobj = new Course();
JSONArray fields = js.getJSONArray("values");
for (int i=0;i<fields.length();i++){
JSONObject temp = fields.getJSONObject(i);
Iterator<String> it = temp.keys();
while(it.hasNext()){
String key = it.next();
String value = temp.getString(key);
Courseobj.key=value;
}
}
}
public static Office readOffice(JSONObject js) throws JSONException{
Office Officeobj = new Office();
JSONArray fields = js.getJSONArray("values");
for (int i=0;i<fields.length();i++){
JSONObject temp = fields.getJSONObject(i);
Iterator<String> it = temp.keys();
while(it.hasNext()){
String key = it.next();
String value = temp.getString(key);
Officeobj.key=value;
}
}
}
public static Faculty readFaculty(JSONObject js) throws JSONException{
Faculty Facultyobj = new Faculty();
JSONArray fields = js.getJSONArray("values");
for (int i=0;i<fields.length();i++){
JSONObject temp = fields.getJSONObject(i);
Iterator<String> it = temp.keys();
while(it.hasNext()){
String key = it.next();
String value = temp.getString(key);
Facultyobj.key=value;
}
}
}
public static Grade readGrade(JSONObject js) throws JSONException{
Grade Gradeobj = new Grade();
JSONArray fields = js.getJSONArray("values");
for (int i=0;i<fields.length();i++){
JSONObject temp = fields.getJSONObject(i);
Iterator<String> it = temp.keys();
while(it.hasNext()){
String key = it.next();
String value = temp.getString(key);
Gradeobj.key=value;
}
}
}
}
