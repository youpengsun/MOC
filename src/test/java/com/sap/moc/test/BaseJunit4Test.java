package com.sap.moc.test;

import javax.transaction.Transactional;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class, loader = AnnotationConfigContextLoader.class)
@Transactional
public class BaseJunit4Test {
	@BeforeClass
	public static void setUp() {
		System.out.println("-----> SETUP <-----");
	}

	@AfterClass
	public static void afterTest() {
		System.out.println("-----> DESTROY <-----");
	}
}
