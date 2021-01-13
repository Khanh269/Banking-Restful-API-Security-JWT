package banking.service;

import java.time.LocalDateTime;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import banking.entity.TransHistory;
import banking.entity.Users;
import banking.repository.TransHistoryRepository;
import banking.repository.UsersRepository;

@Service
public class Services {
	@Autowired
	private PasswordEncoder bcryptEncoder;
	@Autowired
	private UsersRepository repository;
	@Autowired
	private TransHistoryRepository tRepository;

	public Users getCurrentUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String currentUsername = ((UserDetails) principal).getUsername();
		return getUserByName(currentUsername);
	}

	public long getBalance(int userId) {
		Users user = repository.getOne(userId);
		return user.getBalance();
	}

	public Users saveUser(Users user) {
		Users newUser = new Users();
		newUser.setuserName(user.getUserName());
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		return repository.save(newUser);

	}

	public List<Users> getUsers() {
		return repository.findAll();
	}

	public Users getUserById(int userId) {
		return repository.findById(userId).orElse(null);
	}

	public Users getUserByName(String userName) {
		return repository.findByuserName(userName);
	}

	public String deleteUser(int userId) {
		repository.deleteById(userId);
		return "Users removed !! " + userId;
	}

	public Users updateUser(Users user) {
		Users existingUsers = repository.findById(user.getUserId()).orElse(null);
		existingUsers.setuserName(user.getUserName());
		existingUsers.setBalance(user.getBalance());
		return repository.save(existingUsers);
	}

	public JSONObject deposit(int userId, int amount) {
		Users user = getUserById(userId);
		TransHistory tHistory;
		long newBalance;
		boolean success = false;
		try {
			newBalance = user.getBalance() + amount;
			user.setBalance(newBalance);
			repository.save(user);

			tHistory = new TransHistory();
			tHistory.setUser(getUserById(userId));
			tHistory.setAmount(amount);
			tHistory.setTransTime(LocalDateTime.now());
			tRepository.save(tHistory);

			success = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject json = new JSONObject();
		json.put("success", success);
		json.put("user", user);
		return json;
	}

	public JSONObject withdraw(int userId, int amount) {
		Users user = getUserById(userId);
		TransHistory tHistory;
		long newBalance;
		boolean success = false;
		try {
			if (user.getBalance() >= amount) {
				newBalance = user.getBalance() - amount;
				user.setBalance(newBalance);
				repository.save(user);

				tHistory = new TransHistory();
				tHistory.setUser(getUserById(userId));
				tHistory.setAmount(-amount);
				tHistory.setTransTime(LocalDateTime.now());
				tRepository.save(tHistory);

				success = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject json = new JSONObject();
		json.put("success", success);
		json.put("user", user);
		return json;
	}

	public JSONObject transfer(int userTransferId, int userBenefitId, int amount) {
		JSONObject json = new JSONObject();
		try {
			json = withdraw(userTransferId, amount);
			deposit(userBenefitId, amount);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	public List<TransHistory> findByIdPaging(int userId, Integer pageNo, Integer pageSize) {
		Pageable paging = PageRequest.of(pageNo, pageSize);
		Page<TransHistory> pagedResult = tRepository.findById(userId,paging);
		return pagedResult.getContent();
	}
}
