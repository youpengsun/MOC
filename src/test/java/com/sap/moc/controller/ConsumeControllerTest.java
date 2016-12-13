package com.sap.moc.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@Transactional
@ContextConfiguration(locations = { "classpath:/config/spring-core.xml", "classpath:/config/dispatcher-servlet.xml" })
public class ConsumeControllerTest {
	@Autowired
	private WebApplicationContext wac;

	@Mock
	// private IConsumeDAOService service;

	@Autowired
	MockHttpSession session;

	@InjectMocks
	private ConsumeController controller;

	private MockMvc mockMvc;

	private String mediaType;

	@Before
	public void setup() {

		// Process mock annotations
		MockitoAnnotations.initMocks(this);

		// Setup Spring test in standalone mode

		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
		mediaType = "application/json;charset=UTF-8";
		// ee1 = new Employee();
		// ee2 = new Employee();
		//
		// employeeList.add(ee1);
		// employeeList.add(ee2);
	}

//	@Test
	public void testGetEmployeQuery() throws Exception {
		System.out.println("-----------Begin of testGetEmployeQuery-----------");
//		QueryTime time = new QueryTime(2015, 12, 0);

		ResultActions resultActions = mockMvc
				.perform(get("/vendor/settlement").param("vendorId", "123").contentType(mediaType));
		// .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
		resultActions.andExpect(status().isOk()).andExpect(content().contentType(mediaType)).andDo(print());

		System.out.println("-----------End of testGetEmployeQuery-----------");
	}

	@Test
	public void testAnalysis() throws Exception {
		System.out.println("-----------Begin of testAnalysis-----------");
//		QueryTime time = new QueryTime(2015, 12, 0);
		// MockHttpServletRequestBuilder requestBuilder =
		// MockMvcRequestBuilders.post("/vendor/consumeanalysis").contentType(mediaType).body(new
		// String("{\"T1\":109.1, \"T2\":99.3}").getBytes());
		String requestBody = "{\"begda\": {\"year\": 2016,\"month\": 1,},\"endda\": {\"year\": 2016,\"month\":1,}}}";

		ResultActions resultActions = mockMvc
				.perform(post("/vendor/consumeanalysis").content(requestBody).contentType(mediaType));
		// .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
		resultActions.andExpect(
				status().isOk()).andExpect(content().contentType(mediaType)).andDo(print());

		System.out.println("-----------End of testAnalysis-----------");
	}
}
