package edu.leicester;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.leicester.co2103cw3.Co2103Cw3Application;
import edu.leicester.co2103cw3.domain.Convenor;
import edu.leicester.co2103cw3.domain.Lecture;
import edu.leicester.co2103cw3.domain.Module;
import edu.leicester.co2103cw3.repository.ConvenorRepository;
import edu.leicester.co2103cw3.repository.LectureRepository;
import edu.leicester.co2103cw3.repository.ModuleRepository;

@SpringBootTest(classes = Co2103Cw3Application.class)
@AutoConfigureMockMvc
public class CO2103Tests {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ModuleRepository mrepo;
	
	@Autowired
	private LectureRepository lrepo;
	
	@Autowired
	private ConvenorRepository crepo;
	
	/**
	 * curl -X POST -H "Content-Type: application/json" -d '{"code":"CO2102", "title":"Programming Fundamentals", "semester":1, "core":true}' http://localhost:8080/modules
	 */
	@Test
	public void testPostModule() throws Exception {
		Module m = new Module();
		
		ObjectMapper om = new ObjectMapper();
		String content = om.writeValueAsString(m);
		
		this.mockMvc.perform(post("/modules").contentType(MediaType.APPLICATION_JSON)
			.content(content))
			.andDo(print()).andExpect(status().isCreated())
			.andExpect(header().exists("Location"))
			.andExpect(content().contentType(MediaType.APPLICATION_JSON));
		
		assertNotNull(mrepo.findById(1));
	}
	
	/**
	 * curl -X POST -H "Content-Type: application/json" -d '{"code":"CO2102", "title":"Programming Fundamentals", "semester":1, "core":true}' http://localhost:8080/modules
	 */
	@Test
	public void testPostModuleConflict() throws Exception {
		Module m = new Module();
		m = mrepo.save(m);
		
		ObjectMapper om = new ObjectMapper();
		String content = om.writeValueAsString(m);
		
		this.mockMvc.perform(post("/modules").contentType(MediaType.APPLICATION_JSON)
			.content(content))
			.andDo(print()).andExpect(status().isConflict());
	}
	
	/**
	 * curl -X GET http://localhost:8080/modules
	 */
	@Test
	public void testGetAllModules() throws Exception {
		Module m = new Module();
		m = mrepo.save(m);
		
		this.mockMvc.perform(get("/modules"))
			.andDo(print()).andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
	/**
	 * curl -X GET http://localhost:8080/modules
	 */
	@Test
	public void testGetAllModulesNotFound() throws Exception {
		mockMvc.perform(get("/modules")).andExpect(status().isNotFound());
	}

	/**
	 * curl -X GET http://localhost:8080/modules/1
	 */
	@Test
	public void testGetModule() throws Exception {
		Module m = new Module();
		m = mrepo.save(m);
		
		this.mockMvc.perform(get("/modules/{moduleId}", m.getId()))
			.andDo(print()).andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
	/**
	 * curl -X GET http://localhost:8080/modules/50
	 */
	@Test
	public void testGetModuleNotFound() throws Exception {
		this.mockMvc.perform(get("/modules/50"))
		.andDo(print()).andExpect(status().isNotFound());
	}
	
	/**
	 * curl -i -X PUT -H "Content-Type: application/json" -d '{"code":"CO2103", "title":"A new title", "semester":2, "core":false}' http://localhost:8080/modules/1
	 */
	@Test
	public void testPutModule() throws Exception {
		Module m = new Module();
		m = mrepo.save(m);
		
		Module newM = new Module();
		newM.setTitle("New Title");
		
		ObjectMapper om = new ObjectMapper();
		String content = om.writeValueAsString(newM);
		
		MvcResult response = this.mockMvc.perform(put("/modules/{moduleId}", m.getId())
			.contentType(MediaType.APPLICATION_JSON)
			.content(content))
			.andDo(print()).andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();
		
		String responseValue = response.getResponse().getContentAsString();
		
		Module result = om.readValue(responseValue, Module.class);
		assertEquals("New Title", result.getTitle());
	}
	
	/**
	 * curl -i -X PUT -H "Content-Type: application/json" -d '{"code":"CO2103", "title":"A new title", "semester":2, "core":false}' http://localhost:8080/modules/50
	 */
	@Test
	public void testPutModuleNotFound() throws Exception {
		Module newM = new Module();
		newM.setTitle("New Title");
		
		ObjectMapper om = new ObjectMapper();
		String content = om.writeValueAsString(newM);
		
		this.mockMvc.perform(put("/modules/50")
			.contentType(MediaType.APPLICATION_JSON)
			.content(content))
			.andDo(print()).andExpect(status().isNotFound());
	}
	
	/**
	 * curl -X DELETE http://localhost:8080/modules/1
	 */
	@Test
	public void testDeleteModule() throws Exception {
		Module m = new Module();
		m = mrepo.save(m);
		
		this.mockMvc.perform(delete("/modules/{moduleId}", m.getId()))
			.andDo(print()).andExpect(status().isOk());
		
		assertNull(mrepo.findById(m.getId()));
	}
	
	/**
	 * curl -X DELETE http://localhost:8080/modules/50
	 */
	@Test
	public void testDeleteModuleNotFound() throws Exception {
		this.mockMvc.perform(delete("/modules/50"))
			.andDo(print()).andExpect(status().isNotFound());
	}
	
	/**
	 * curl -X POST -H "Content-Type: application/json" -d '{"name":"Elliot", "position":"Lecturer", "office":"Informatics"}' http://localhost:8080/convenors
	 */
	@Test
	public void testPostConvenor() throws Exception {
		ObjectMapper om = new ObjectMapper();
		String content = om.writeValueAsString(new Convenor());
		
		this.mockMvc.perform(post("/convenors").contentType(MediaType.APPLICATION_JSON)
			.content(content))
			.andDo(print()).andExpect(status().isCreated())
			.andExpect(header().exists("Location"))
			.andExpect(content().contentType(MediaType.APPLICATION_JSON));
		
		assertNotNull(crepo.findById(1));
	}
	
	/**
	 * curl -X POST -H "Content-Type: application/json" -d '{"name":"Elliot", "position":"Lecturer", "office":"Informatics"}' http://localhost:8080/convenors
	 */
	@Test
	public void testPostConvenorConflict() throws Exception {
		Convenor c = new Convenor();
		c = crepo.save(c);
		
		ObjectMapper om = new ObjectMapper();
		String content = om.writeValueAsString(c);
		
		this.mockMvc.perform(post("/convenors").contentType(MediaType.APPLICATION_JSON)
			.content(content))
			.andDo(print()).andExpect(status().isConflict());
	}
	
	/**
	 * curl -X GET http://localhost:8080/convenors
	 */
	@Test
	public void testGetAllConvenors() throws Exception {
		Convenor c = new Convenor();
		c = crepo.save(c);
		
		this.mockMvc.perform(get("/convenors"))
			.andDo(print()).andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
	/**
	 * curl -X GET http://localhost:8080/convenors
	 */
	@Test
	public void testGetAllConvenorsNotFound() throws Exception {
		mockMvc.perform(get("/convenors")).andExpect(status().isNotFound());
	}
	
	/**
	 * curl -X GET http://localhost:8080/convenors/1
	 */
	@Test
	public void testGetConvenor() throws Exception {
		Convenor c = new Convenor();
		c = crepo.save(c);
		
		this.mockMvc.perform(get("/convenors/{convenorId}", c.getId()))
			.andDo(print()).andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
	/**
	 * curl -X GET http://localhost:8080/convenors/50
	 */
	@Test
	public void testGetConvenorNotFound() throws Exception {
		this.mockMvc.perform(get("/convenors/50"))
		.andDo(print()).andExpect(status().isNotFound());
	}
	
	/**
	 * curl -X PUT -H "Content-Type: application/json" -d '{"name":"John", "position":"GTA", "office":"Informatics"}' http://localhost:8080/convenors/1
	 */
	@Test
	public void testPutConvenor() throws Exception {
		Convenor c = new Convenor();
		c = crepo.save(c);
		
		Convenor newC = new Convenor();
		newC.setName("New Name");
		
		ObjectMapper om = new ObjectMapper();
		String content = om.writeValueAsString(newC);
		
		MvcResult response = this.mockMvc.perform(put("/convenors/{convenorId}", c.getId())
			.contentType(MediaType.APPLICATION_JSON)
			.content(content))
			.andDo(print()).andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();
		
		String responseValue = response.getResponse().getContentAsString();
		
		Convenor result = om.readValue(responseValue, Convenor.class);
		assertEquals("New Name", result.getName());
	}
	
	/**
	 * curl -X PUT -H "Content-Type: application/json" -d '{"name":"John", "position":"GTA", "office":"Informatics"}' http://localhost:8080/convenors/50
	 */
	@Test
	public void testPutConvenorNotFound() throws Exception {
		Convenor newC = new Convenor();
		newC.setName("New Name");
		
		ObjectMapper om = new ObjectMapper();
		String content = om.writeValueAsString(newC);
		
		this.mockMvc.perform(put("/convenors/50")
			.contentType(MediaType.APPLICATION_JSON)
			.content(content))
			.andDo(print()).andExpect(status().isNotFound());
	}
	
	/**
	 * curl -X DELETE http://localhost:8080/convenors/1
	 */
	@Test
	public void testDeleteConvenor() throws Exception {
		Convenor c = new Convenor();
		c = crepo.save(c);
		
		this.mockMvc.perform(delete("/convenors/{convenorId}", c.getId()))
			.andDo(print()).andExpect(status().isOk());
		
		assertNull(crepo.findById(c.getId()));
	}
	
	/**
	 * curl -X DELETE http://localhost:8080/convenors/50
	 */
	@Test
	public void testDeleteConvenorNotFound() throws Exception {
		this.mockMvc.perform(delete("/convenors/50"))
			.andDo(print()).andExpect(status().isNotFound());
	}
	
	/**
	 * curl -X POST -H "Content-Type: application/json" -d '{"week":1, "title":"Intro to Programming", "url":"www.google.com"}' http://localhost:8080/modules/1/lectures
	 */
	@Test
	public void testPostLecture() throws Exception {
		Module m = new Module();
		m = mrepo.save(m);
		
		ObjectMapper om = new ObjectMapper();
		String content = om.writeValueAsString(new Lecture());
		
		this.mockMvc.perform(post("/modules/{moduleId}/lectures", m.getId()).contentType(MediaType.APPLICATION_JSON)
			.content(content))
			.andDo(print()).andExpect(status().isCreated())
			.andExpect(header().exists("Location"))
			.andExpect(content().contentType(MediaType.APPLICATION_JSON));
		
		assertNotNull(lrepo.findById(1));
	}
	
	/**
	 * curl -X POST -H "Content-Type: application/json" -d '{"week":1, "title":"Intro to Programming", "url":"www.google.com"}' http://localhost:8080/modules/1/lectures
	 */
	@Test
	public void testPostLectureConflict() throws Exception {
		Module m = new Module();
		m = mrepo.save(m);
		
		Lecture l = new Lecture();
		l = lrepo.save(l);
		
		ObjectMapper om = new ObjectMapper();
		String content = om.writeValueAsString(l);
		
		this.mockMvc.perform(post("/modules/{moduleId}/lectures", m.getId()).contentType(MediaType.APPLICATION_JSON)
			.content(content))
			.andDo(print()).andExpect(status().isConflict());
	}
	
	/**
	 * curl -X GET http://localhost:8080/modules/1/lectures
	 */
	@Test
	public void testGetAllLectures() throws Exception {
		Module m = new Module();
		Lecture l = new Lecture();
		List<Lecture> lectures = new ArrayList<>();
		lectures.add(l);
		m.setLectures(lectures);
		
		l = lrepo.save(l);
		m = mrepo.save(m);
		
		this.mockMvc.perform(get("/modules/{moduleId}/lectures", m.getId()))
			.andDo(print()).andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
	/**
	 * curl -X GET http://localhost:8080/modules/1/lectures
	 */
	@Test
	public void testGetAllLecturesNotFound() throws Exception {
		Module m = new Module();
		m = mrepo.save(m);
		
		mockMvc.perform(get("/modules/{moduleId}/lectures", m.getId())).andExpect(status().isNotFound());
	}
	
	/**
	 * curl -X GET http://localhost:8080/modules/1/lectures/1
	 */
	@Test
	public void testGetLecture() throws Exception {
		Module m = new Module();
		Lecture l = new Lecture();
		List<Lecture> lectures = new ArrayList<>();
		lectures.add(l);
		m.setLectures(lectures);
		
		l = lrepo.save(l);
		m = mrepo.save(m);
		
		this.mockMvc.perform(get("/modules/{moduleId}/lectures/{lectureId}", m.getId(), l.getId()))
			.andDo(print()).andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
	/**
	 * curl -X GET http://localhost:8080/modules/1/lectures/50
	 */
	@Test
	public void testGetLectureNotFound() throws Exception {
		Module m = new Module();
		m = mrepo.save(m);
		
		this.mockMvc.perform(get("/modules/{moduleId}/lectures/50", m.getId()))
		.andDo(print()).andExpect(status().isNotFound());
	}
	
	/**
	 * curl -X PUT -H "Content-Type: application/json" -d '{"week":2, "title":"Programming 2", "url":"www.blackboard.com"}' http://localhost:8080/modules/1/lectures/1
	 */
	@Test
	public void testPutLecture() throws Exception {
		Module m = new Module();
		Lecture l = new Lecture();
		List<Lecture> lectures = new ArrayList<>();
		lectures.add(l);
		m.setLectures(lectures);
		
		l = lrepo.save(l);
		m = mrepo.save(m);
		
		Lecture newL = new Lecture();
		newL.setTitle("New Title");
		
		ObjectMapper om = new ObjectMapper();
		String content = om.writeValueAsString(newL);
		
		MvcResult response = this.mockMvc.perform(put("/modules/{moduleId}/lectures/{lectureId}", m.getId(), l.getId())
			.contentType(MediaType.APPLICATION_JSON)
			.content(content))
			.andDo(print()).andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();
		
		String responseValue = response.getResponse().getContentAsString();
		
		Lecture result = om.readValue(responseValue, Lecture.class);
		assertEquals("New Title", result.getTitle());
	}
	
	/**
	 * curl -X PUT -H "Content-Type: application/json" -d '{"week":2, "title":"Programming 2", "url":"www.blackboard.com"}' http://localhost:8080/modules/1/lectures/50
	 */
	@Test
	public void testPutLectureNotFound() throws Exception {
		Module m = new Module();
		m = mrepo.save(m);
		
		Lecture newL = new Lecture();
		newL.setTitle("New Title");
		
		ObjectMapper om = new ObjectMapper();
		String content = om.writeValueAsString(newL);
		
		this.mockMvc.perform(put("/modules/{moduleId}/lectures/50", m.getId())
			.contentType(MediaType.APPLICATION_JSON)
			.content(content))
			.andDo(print()).andExpect(status().isNotFound());
	}
	
	/**
	 * curl -X DELETE http://localhost:8080/modules/1/lectures/1
	 */
	@Test
	public void testDeleteLecture() throws Exception {
		Module m = new Module();
		Lecture l = new Lecture();
		List<Lecture> lectures = new ArrayList<>();
		lectures.add(l);
		m.setLectures(lectures);
		
		l = lrepo.save(l);
		m = mrepo.save(m);
		
		this.mockMvc.perform(delete("/modules/{moduleId}/lectures/{lectureId}", m.getId(), l.getId()))
			.andDo(print()).andExpect(status().isOk());
		
		assertNull(lrepo.findById(l.getId()));
	}
	
	/**
	 * curl -X DELETE http://localhost:8080/modules/1/lectures/50
	 */
	@Test
	public void testDeleteLectureNotFound() throws Exception {
		Module m = new Module();
		m = mrepo.save(m);
		
		this.mockMvc.perform(delete("/modules/{moduleId}/lectures/50", m.getId()))
			.andDo(print()).andExpect(status().isNotFound());
	}
}
