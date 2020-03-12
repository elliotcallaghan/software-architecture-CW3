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

import edu.leicester.co2103cw3.domain.Lecture;
import edu.leicester.co2103cw3.domain.Module;
import edu.leicester.co2103cw3.repository.LectureRepository;
import edu.leicester.co2103cw3.repository.ModuleRepository;

@RestController
public class LectureController {

	@Autowired
	private LectureRepository lrepo;
	
	@Autowired
	private ModuleRepository mrepo;

	@GetMapping("/modules/{moduleId}/lectures")
	public ResponseEntity<List<Lecture>> listAllLectures(@PathVariable("moduleId") int moduleId) {
		Module module = mrepo.findById(moduleId);
		List<Lecture> lectures = module.getLectures();
		
		if (lectures.isEmpty()) {
			return new ResponseEntity("No lectures found.", HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<List<Lecture>>(lectures, HttpStatus.OK);
	}
	
	@PostMapping("/modules/{moduleId}/lectures")
	public ResponseEntity<?> createLecture(@PathVariable("moduleId") int moduleId, @RequestBody Lecture lecture, UriComponentsBuilder ucBuilder) {
		Module module = mrepo.findById(moduleId);
		
		if (lrepo.existsById(lecture.getId())) {
			return new ResponseEntity("A lecture with that ID already exists.", HttpStatus.CONFLICT);
		}
		
		module.getLectures().add(lecture);
		lecture = lrepo.save(lecture);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/modules/{moduleId}/lectures/{lectureId}").buildAndExpand(moduleId, lecture.getId()).toUri());
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}
	
	@GetMapping("/modules/{moduleId}/lectures/{lectureId}")
	public ResponseEntity<?> getLecture(@PathVariable("moduleId") int moduleId, @PathVariable("lectureId") int lectureId) {
		Module module = mrepo.findById(moduleId);
		List<Lecture> lectures = module.getLectures();
		
		for (Lecture lecture : lectures) {
			if (lecture.getId() == lectureId) {
				return new ResponseEntity<Lecture>(lecture, HttpStatus.OK);
			}
		}
		
		return new ResponseEntity("A lecture with the specified ID was not found.", HttpStatus.NOT_FOUND);
	}

	@PutMapping("/modules/{moduleId}/lectures/{lectureId}")
	public ResponseEntity<?> updateLecture(@PathVariable("moduleId") int moduleId, @PathVariable("lectureId") int lectureId, @RequestBody Lecture lecture) {
		Module module = mrepo.findById(moduleId);
		List<Lecture> lectures = module.getLectures();
		
		for (Lecture currentLecture : lectures) {
			if (currentLecture.getId() == lectureId) {
				currentLecture.setWeek(lecture.getWeek());
				currentLecture.setTitle(lecture.getTitle());
				currentLecture.setUrl(lecture.getUrl());
				currentLecture = lrepo.save(currentLecture);
				
				return new ResponseEntity<Lecture>(currentLecture, HttpStatus.OK);
			}
		}
		return new ResponseEntity("A lecture with the specified ID was not found.", HttpStatus.NOT_FOUND);
	}
	
	@DeleteMapping("/modules/{moduleId}/lectures/{lectureId}")
	public ResponseEntity<?> deleteLecture(@PathVariable("moduleId") int moduleId, @PathVariable("lectureId") int lectureId) {
		Module module = mrepo.findById(moduleId);
		List<Lecture> lectures = module.getLectures();
		
		for (Lecture lecture : lectures) {
			if (lecture.getId() == lectureId) {
				lrepo.deleteById(lectureId);
				return new ResponseEntity("Lecture has been deleted.", HttpStatus.OK);
			}
		}
		
		return new ResponseEntity("A lecture with the specified ID was not found.", HttpStatus.NOT_FOUND);
	}
	
}
