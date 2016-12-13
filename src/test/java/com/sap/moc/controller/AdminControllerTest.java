package com.sap.moc.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.http.MediaType;
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
@ContextConfiguration(locations = { "classpath:/config/spring-core.xml",
		"classpath:/config/dispatcher-servlet.xml" })
public class AdminControllerTest {

	@Autowired
	private WebApplicationContext wac;

	@Mock
//	private IConsumeDAOService service;

	@Autowired
	MockHttpSession session;

	@InjectMocks
	private AdminController controller;

	private MockMvc mockMvc;

//	private Employee ee1;
//	private Employee ee2;
//	private List<Employee> employeeList;
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

	@Test
	public void testGetEmployeeList() throws Exception {
		// when(service.queryAllEmployee()).thenReturn(employeeList);
		System.out.println("-----------Begin of testGetEmployeeList-----------");
		ResultActions resultActions = this.mockMvc.perform(get("/emp/list").contentType(mediaType));
		resultActions.andExpect(status().isOk()).andExpect(content().contentType(mediaType)).andDo(print());

		System.out.println("-----------End of testGetEmployeeList-----------");
	}

	@Test
	public void testGetEmployeQuery() throws Exception {
		System.out.println("-----------Begin of testGetEmployeQuery-----------");

		ResultActions resultActions = mockMvc
				.perform(get("/emp/query").param("lastName", "test").contentType(mediaType));
		// .accept(MediaType.parseMediaType("application/json;charset=UTF-8"))
		resultActions.andExpect(status().isOk()).andExpect(content().contentType(mediaType)).andDo(print());

		System.out.println("-----------End of testGetEmployeQuery-----------");
	}

	 @Test
	 public void testSave() throws Exception {
	 this.mockMvc.perform(get("/emp/list").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
	 .andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"));
	 }
	
	// @Test
	// public void testEdit() throws Exception {
	// this.mockMvc.perform(get("/emp/edit").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
	// .andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"));
	// }
	//
	// @Test
	// public void testDel() throws Exception {
	// this.mockMvc.perform(get("/emp/del").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
	// .andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"));
	// }
	//
	// @Test
	// public void testGetPage() throws Exception {
	// this.mockMvc.perform(get("/emp/query").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
	// .andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"));
	// }
	//
	 @Test
	 public void testExcelUpload() throws Exception {
//	 FileInputStream fi
	
	
//	 mockMvc.perform(MockMvcRequestBuilders.fileUpload("/emp/excelupload")
//	 .file(upload)
//	 this.mockMvc.perform(get("/emp/excelupload").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
//	 .andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"));
		 
//		 mockMvc().perform(
//				  fileUpload("/upload")
//				  .file(FileFactory.stringContent("myFile"))
//				  .with(new RequestPostProcessor() { 
//				    public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
//				       request.setRemoteAddr("12345"); 
//				       return request;
//				    }}));
	 }

}
