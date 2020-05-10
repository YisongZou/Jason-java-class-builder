package edu.duke.ece651.classbuilder;

import java.util.Iterator;
import java.io.ByteArrayInputStream;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.Test;

public class ClassBuilderTest {
  @Test
  public void test_() {
    ReadJson input = new ReadJson();
    String json = "null";
		try {
			json = input.read_json("/home/yisongzou/ece651/yz558-json-651/src/test/resources/inputs/valid/array.json");
		} catch (IOException e) {
					e.printStackTrace();
		}
    Collection<String> ans1 = new ArrayList<String>();
   Collection<String> ans2 = new ArrayList<String>();
        ClassBuilder str = new ClassBuilder(json);
        InputStream is = new ByteArrayInputStream(json.getBytes());
		   ClassBuilder stream = new ClassBuilder(is);
          ans1 = str.getClassNames();
          ans2 = stream.getClassNames();
    // Iterate over all elements in the list and test hte getClassNames method
          for(String s0: ans1) {
            System.out.println(s0);
    }
          
         for(String s1: ans2) {
            System.out.println(s1);
    }
         //Testing the getSourceCode method
         for(String cn: ans1){
           String source = str.getSourceCode(cn);
           System.out.println(source);
         }
         str.createAllClasses("/home/yisongzou/ece651/yz558-json-651/src/main/java");
  }
}




