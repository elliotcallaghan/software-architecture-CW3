package edu.leicester.co2103cw3.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import edu.leicester.co2103cw3.domain.Module;

public interface ModuleRepository extends CrudRepository<Module, Integer> {

	public List<Module> findAll();
	public Module findById(int id);
	public boolean existsByTitle(String title);

}
