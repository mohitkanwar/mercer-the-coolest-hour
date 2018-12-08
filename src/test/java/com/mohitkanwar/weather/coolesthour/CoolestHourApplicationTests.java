package com.mohitkanwar.weather.coolesthour;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@RunWith(JUnit4.class)
@SpringBootTest({"94043"})
public class CoolestHourApplicationTests {
    @Autowired
    ApplicationContext ctx;

    @Test
    public void testRun() {
      //  String[] arg = {"94043"};
        //CoolestHourApplication.main(arg);
    }


}
