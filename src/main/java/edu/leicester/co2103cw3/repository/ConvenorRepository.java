package edu.leicester.co2103cw3.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import edu.leicester.co2103cw3.domain.Convenor;

public interface ConvenorRepository extends CrudRepository<Convenor, Integer> {

	public List<Convenor> findAll();
	public Convenor findById(int id);
	public boolean existsByName(String name);
}
