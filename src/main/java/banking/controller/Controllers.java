package banking.controller;

import banking.model.TransHistory;
import banking.model.UserDto;
import banking.model.Users;
import banking.service.Services;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Controllers {

	@Autowired
	private Services service;

	@PostMapping("/register")
	public Users register(@RequestBody Users user) {
		return service.saveUser(user);
	}

	@GetMapping("/users")
	public List<Users> findAllUserss(@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		return service.getUsersPaging(pageNo,pageSize);
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
	public UserDto deposit(@RequestParam int amount) {
		Users user = service.getCurrentUser();
		return service.deposit(user.getUserId(), amount);
	}

	@PostMapping("/withdraw")
	public UserDto withdraw(@RequestParam int amount) {
		Users user = service.getCurrentUser();
		return service.withdraw(user.getUserId(), amount);
	}

	@PostMapping("/transfer")
	public UserDto transfer(@RequestParam int userId, int amount) {
		Users user = service.getCurrentUser();
		return service.transfer(user.getUserId(), userId, amount);
	}

	@GetMapping("/transferHistory")
	public List<TransHistory> transHistory(@RequestParam int userId,
			@RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
		List<TransHistory> list = service.transHistoryByUserIdPaging (userId, pageNo, pageSize);
		return list; 
	}

}
