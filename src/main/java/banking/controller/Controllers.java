package banking.controller;

import banking.entity.TransHistory;
import banking.entity.Users;
import banking.service.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Controllers {
	

	@Autowired
	private Services service;
	

	@PostMapping("/register")
	public Users register (@RequestBody Users user) {
		return service.saveUser(user);
	}

	@GetMapping("/users")
	public List<Users> findAllUserss() {
		return service.getUsers();
	}

	@GetMapping("/userById/{userId}")
	public Users findUsersById(@PathVariable int userId) {
		return service.getUserById(userId);
	}

	@PutMapping("/update")
	public Users updateUser(@RequestBody Users user) {
		return service.updateUser(user);
	}

	@DeleteMapping("/delete")
	public String deleteUsers(@RequestParam int userId) {
		return service.deleteUser(userId);
	}

	@PostMapping("/deposit")
	public String deposit(@RequestParam int amount) {
		Users user = service.getCurrentUser();
		return service.deposit(user.getUserId(), amount);
	}

	@PostMapping("/withdraw")
	public String withdraw(@RequestParam int amount) {
		Users user = service.getCurrentUser();
		return service.withdraw(user.getUserId(), amount);
	}

	@PostMapping("/transfer")
	public String transfer (@RequestParam int userId, int amount) {
		Users user = service.getCurrentUser();
		return service.transfer(user.getUserId(), userId, amount);
	}

	@GetMapping("/transferHistory")
	public List<TransHistory> transHistory(@RequestParam int userId) {
		return service.transHistory(userId);
	}
	

	
}
