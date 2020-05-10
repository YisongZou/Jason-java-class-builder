package edu.duke.ece651.classbuilder;
import java.util.*;
import org.json.*;
import java.io.*;
public class Grade{
private Course course;
public Course getCourse(){
return course;
}
public void setCourse(Course course){
this.course=course;
}
private String student;
public String getStudent(){
return student;
}
public void setStudent(String student){
this.student=student;
}
private double grade;
public double getGrade(){
return grade;
}
public void setGrade(double grade){
this.grade=grade;
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
ans.put("type","Grade");
unique_ids.put(this, unique_ids.size() + 1 );//object
JSONObject courseobj = new JSONObject();
if(unique_ids.get(course) == null){
courseobj.put("course",course.jsonhelper(unique_ids));
values.put(courseobj);
}
else{
courseobj.put("ref",unique_ids.get(course));
values.put(courseobj);
}
//normal
JSONObject studentobj = new JSONObject();
studentobj.put("student",student);
values.put(studentobj);
//normal
JSONObject gradeobj = new JSONObject();
gradeobj.put("grade",grade);
values.put(gradeobj);
ans.put("values",values);
return ans;
}
}
