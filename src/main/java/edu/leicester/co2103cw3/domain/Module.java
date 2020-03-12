package edu.leicester.co2103cw3.domain;

import java.util.Comparator;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Module {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String code;
	
	private String title;
	
	private int semester;
	
	private boolean core;
	
	@ManyToOne
	private Convenor convenor;
	
	@OneToMany
	@JoinColumn(name="module_id")
	private List<Lecture> lectures;
	
	public Module() {
		setSemester(1);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getSemester() {
		return semester;
	}

	public void setSemester(int semester) {
		if (semester < 1 || semester > 10) {
			throw new IllegalArgumentException("Semester must be between 1 and 10");
		}
		this.semester = semester;
	}

	public boolean isCore() {
		return core;
	}

	public void setCore(boolean core) {
		this.core = core;
	}

	public Convenor getConvenor() {
		return convenor;
	}

	public void setConvenor(Convenor convenor) {
		this.convenor = convenor;
	}

	public List<Lecture> getLectures() {
		if (lectures != null) {
			lectures.sort(Comparator.comparing(Lecture::getWeek));
		}
		return lectures;
	}

	public void setLectures(List<Lecture> lectures) {
		this.lectures = lectures;
	}
	
}
