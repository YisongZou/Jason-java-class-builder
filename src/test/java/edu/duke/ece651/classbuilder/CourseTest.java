package edu.duke.ece651.classbuilder;

import java.util.ArrayList;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

public class CourseTest {
  @Test
  public void test_() {
    Course course = new Course();
    Faculty faculty = new Faculty();
    faculty.setName("www");
    faculty.addTaught(course);
    course.setInstructor(faculty);
    course.setNumStudents(5);

    course.addGrades(new Grade());
    Grade gradeDD = course.getGrades(0);
    gradeDD.setCourse(course);
    gradeDD.setStudent("eee");
    gradeDD.setGrade(99);

    course.addGrades(new Grade());
    Grade gradeee = course.getGrades(1);
    gradeee.setCourse(course);
    gradeee.setStudent("qqq");
    gradeee.setGrade(100);

    JSONObject ans = new JSONObject();
    ans = course.toJSON();
    System.out.println(ans);
  }

}


