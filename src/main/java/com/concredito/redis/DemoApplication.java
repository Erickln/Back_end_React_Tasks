package com.concredito.redis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.concredito.redis.demo.entity.User;
import com.concredito.redis.demo.repository.UserRepository;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@SpringBootApplication
@RestController
@RequestMapping("/user")
public class DemoApplication {

	@Autowired
	private UserRepository userRepository;

	@PostMapping
	public User save(@RequestBody User user) {
		return userRepository.save(user);
	}

	@GetMapping
	public List<User> list() {
		return userRepository.findAll();
	}

	@GetMapping("/{id}")
	public User findById(@PathVariable String id) {
		return userRepository.findById(id);
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable String id) {
		return userRepository.delete(id);
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
