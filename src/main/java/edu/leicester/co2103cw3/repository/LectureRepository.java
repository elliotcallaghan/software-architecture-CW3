package edu.leicester.co2103cw3.repository;

import org.springframework.data.repository.CrudRepository;

import edu.leicester.co2103cw3.domain.Lecture;

public interface LectureRepository extends CrudRepository<Lecture, Integer> {

	public Lecture findById(int id);
	public boolean existsByTitle(String title);

}
