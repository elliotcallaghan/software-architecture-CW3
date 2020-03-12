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

import edu.leicester.co2103cw3.domain.Convenor;
import edu.leicester.co2103cw3.repository.ConvenorRepository;

@RestController
public class ConvenorController {

	@Autowired
	private ConvenorRepository crepo;

	@GetMapping("/convenors")
	public ResponseEntity<List<Convenor>> listAllConvenors() {
		List<Convenor> convenors = crepo.findAll();
		
		if (convenors.isEmpty()) {
			return new ResponseEntity("No convenors found.", HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<List<Convenor>>(convenors, HttpStatus.OK);
	}

	@PostMapping("/convenors")
	public ResponseEntity<?> createConvenor(@RequestBody Convenor convenor, UriComponentsBuilder ucBuilder) {
		if (crepo.existsById(convenor.getId())) {
			return new ResponseEntity("A convenor with that ID already exists.", HttpStatus.CONFLICT);
		}

		convenor = crepo.save(convenor);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/convenors/{convenorId}").buildAndExpand(convenor.getId()).toUri());
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	@GetMapping("/convenors/{convenorId}")
	public ResponseEntity<?> getConvenor(@PathVariable("convenorId") int convenorId) {
		Convenor convenor = crepo.findById(convenorId);

		if (convenor == null) {
			return new ResponseEntity("A convenor with the specified ID was not found.", HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Convenor>(convenor, HttpStatus.OK);
	}

	@PutMapping("/convenors/{convenorId}")
	public ResponseEntity<?> updateConvenor(@PathVariable("convenorId") int convenorId, @RequestBody Convenor convenor) {
		Convenor currentConvenor = crepo.findById(convenorId);

		if (currentConvenor == null) {
			return new ResponseEntity("A convenor with the specified ID was not found.", HttpStatus.NOT_FOUND);
		}

		currentConvenor.setName(convenor.getName());
		currentConvenor.setPosition(convenor.getPosition());
		currentConvenor.setOffice(convenor.getOffice());
		currentConvenor = crepo.save(currentConvenor);
		
		return new ResponseEntity<Convenor>(currentConvenor, HttpStatus.OK);
	}

	@DeleteMapping("/convenors/{convenorId}")
	public ResponseEntity<?> deleteConvenor(@PathVariable("convenorId") int convenorId) {
		Convenor convenor = crepo.findById(convenorId);

		if (convenor == null) {
			return new ResponseEntity("A convenor with the specified ID was not found.", HttpStatus.NOT_FOUND);
		}

		crepo.deleteById(convenorId);
		return new ResponseEntity("Convenor has been deleted.", HttpStatus.OK);
	}

}
