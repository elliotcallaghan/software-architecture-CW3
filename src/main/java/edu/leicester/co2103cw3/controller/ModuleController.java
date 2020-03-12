package edu.leicester.co2103cw3.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import edu.leicester.co2103cw3.domain.Module;
import edu.leicester.co2103cw3.repository.ModuleRepository;

@RestController
public class ModuleController {

	@Autowired
	private ModuleRepository mrepo;

	@GetMapping("/modules")
	public ResponseEntity<List<Module>> listAllModules() {
		List<Module> modules = mrepo.findAll();
		
		if (modules.isEmpty()) {
			return new ResponseEntity("No modules found.", HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<List<Module>>(modules, HttpStatus.OK);
	}

	@PostMapping("/modules")
	public ResponseEntity<?> createModule(@RequestBody Module module, UriComponentsBuilder ucBuilder) {
		if (mrepo.existsById(module.getId())) {
			return new ResponseEntity("A module with that ID already exists.", HttpStatus.CONFLICT);
		}

		module = mrepo.save(module);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/modules/{moduleId}").buildAndExpand(module.getId()).toUri());
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	@GetMapping("/modules/{moduleId}")
	public ResponseEntity<?> getModule(@PathVariable("moduleId") int moduleId) {
		Module module = mrepo.findById(moduleId);

		if (module == null) {
			return new ResponseEntity("A module with the specified ID was not found.", HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Module>(module, HttpStatus.OK);
	}

	@PutMapping("/modules/{moduleId}")
	public ResponseEntity<?> updateModule(@PathVariable("moduleId") int moduleId, @RequestBody Module module) {
		Module currentModule = mrepo.findById(moduleId);

		if (currentModule == null) {
			return new ResponseEntity("A module with the specified ID was not found.", HttpStatus.NOT_FOUND);
		}

		currentModule.setCode(module.getCode());
		currentModule.setTitle(module.getTitle());
		currentModule.setSemester(module.getSemester());
		currentModule.setCore(module.isCore());
		currentModule.setConvenor(module.getConvenor());
		currentModule.setLectures(module.getLectures());
		currentModule = mrepo.save(currentModule);
		
		return new ResponseEntity<Module>(currentModule, HttpStatus.OK);
	}

	@DeleteMapping("/modules/{moduleId}")
	public ResponseEntity<?> deleteModule(@PathVariable("moduleId") int moduleId) {
		Module module = mrepo.findById(moduleId);

		if (module == null) {
			return new ResponseEntity("A module with the specified ID was not found.", HttpStatus.NOT_FOUND);
		}

		mrepo.deleteById(moduleId);
		return new ResponseEntity("Module has been deleted.", HttpStatus.OK);
	}

}
