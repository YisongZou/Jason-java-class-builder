package edu.duke.ece651.classbuilder;
import java.util.*;
import org.json.*;
import java.io.*;
public class Course{
private Faculty instructor;
public Faculty getInstructor(){
return instructor;
}
public void setInstructor(Faculty instructor){
this.instructor=instructor;
}
private int numStudents;
public int getNumStudents(){
return numStudents;
}
public void setNumStudents(int numStudents){
this.numStudents=numStudents;
}
private ArrayList<Grade> grades;
public int numGrades(){
return grades.size();
}
public void addGrades(Grade n){
grades.add(n);
}
public Grade getGrades(int index){
return grades.get(index);
}
public void setGrades(int index, Grade n){
grades.set(index, n);
}
public Course(){
grades = new ArrayList<Grade>();
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
ans.put("type","Course");
unique_ids.put(this, unique_ids.size() + 1 );//object
JSONObject instructorobj = new JSONObject();
if(unique_ids.get(instructor) == null){
instructorobj.put("instructor",instructor.jsonhelper(unique_ids));
values.put(instructorobj);
}
else{
instructorobj.put("ref",unique_ids.get(instructor));
values.put(instructorobj);
}
//normal
JSONObject numStudentsobj = new JSONObject();
numStudentsobj.put("numStudents",numStudents);
values.put(numStudentsobj);
//array of class
JSONObject gradesobj = new JSONObject();
JSONArray gradesarray = new JSONArray();
for (Grade temp : grades) {
if(unique_ids.get(temp) == null){
gradesobj.put("grades",temp.jsonhelper(unique_ids));
gradesarray.put(gradesobj);
}
else{
gradesobj.put("ref",unique_ids.get(temp));
gradesarray.put(gradesobj);
}
}
values.put(gradesarray);
ans.put("values",values);
return ans;
}
}
