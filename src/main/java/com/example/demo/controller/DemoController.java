package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Demo;
import com.example.demo.repository.DemoRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class DemoController {
    
    @Autowired
    DemoRepository demoRepository;

    @GetMapping("/demo")
    public ResponseEntity<List<Demo>> getAllDemo(@RequestParam(required = false) String title) {
        try {
			List<Demo> tutorials = new ArrayList<Demo>();

			if (title == null)
				demoRepository.findAll().forEach(tutorials::add);
			else
				demoRepository.findByTitleContaining(title).forEach(tutorials::add);

			if (tutorials.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(tutorials, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    @GetMapping("/demo/{id}")
	public ResponseEntity<Demo> getTutorialById(@PathVariable("id") long id) {
		Optional<Demo> tutorialData = demoRepository.findById(id);

		if (tutorialData.isPresent()) {
			return new ResponseEntity<>(tutorialData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

    @PostMapping("/demo")
	public ResponseEntity<Demo> createDemo(@RequestBody Demo demo) {
		try {
			Demo _demo = demoRepository
					.save(new Demo(demo.getTitle(), demo.getDescription(), demo.isPublished()));
			return new ResponseEntity<>(_demo, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

    @PutMapping("/demo/{id}")
	public ResponseEntity<Demo> updateDemo(@PathVariable("id") long id, @RequestBody Demo demo) {
		Optional<Demo> tutorialData = demoRepository.findById(id);

		if (tutorialData.isPresent()) {
			Demo _demo = tutorialData.get();
			_demo.setTitle(demo.getTitle());
			_demo.setDescription(demo.getDescription());
			_demo.setPublished(demo.isPublished());
			return new ResponseEntity<>(demoRepository.save(_demo), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

    @DeleteMapping("/demo/{id}")
	public ResponseEntity<HttpStatus> deleteDemo(@PathVariable("id") long id) {
		try {
			demoRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/demo")
	public ResponseEntity<HttpStatus> deleteAllDemo() {
		try {
			demoRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/demo/published")
	public ResponseEntity<List<Demo>> findByPublished() {
		try {
			List<Demo> demos = demoRepository.findByPublished(true);

			if (demos.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(demos, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
