package banking.service;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import banking.model.TransHistory;
import banking.model.UserDto;
import banking.model.Users;
import banking.repository.TransHistoryRepository;
import banking.repository.UsersRepository;

@Service
public class ServicesImpl implements Services {
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

	public Users saveUser(Users user) {
		Users newUser = new Users();
		newUser.setUserName(user.getUserName());
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		return repository.save(newUser);
	}

	public List<Users> getUsersPaging(int pageNo, int pageSize) {
		Pageable paging = PageRequest.of(pageNo, pageSize);
		Page<Users> pagedResult = repository.findAll(paging);
		return pagedResult.getContent();
	}

	public Users getUserById(int userId) {
		return repository.findById(userId).orElse(null);
	}

	public Users getUserByName(String userName) {
		return repository.findByuserName(userName);
	}

	public Users updateUser(Users user) {
		Users existingUsers = repository.findById(user.getUserId()).orElse(null);
		existingUsers.setUserName(user.getUserName());
		existingUsers.setBalance(user.getBalance());
		return repository.save(existingUsers);
	}

	public String deleteUser(int userId) {
		repository.deleteById(userId);
		return "Users removed !! " + userId;
	}

	@Override
	public UserDto deposit(int userId, int amount) {
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
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(user, UserDto.class);
		userDto.setSuccess(success);
		return userDto;
	}

	@Override
	public UserDto withdraw(int userId, int amount) {
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
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(user, UserDto.class);
		userDto.setSuccess(success);
		return userDto;
	}

	@Override
	public UserDto transfer(int userTransferId, int userBenefitId, int amount) {
		UserDto userDto = new UserDto();
		try {
			userDto = withdraw(userTransferId, amount);
			deposit(userBenefitId, amount);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userDto;
	}

	@Override
	public List<TransHistory> transHistoryByUserIdPaging(int userId, Integer pageNo, Integer pageSize) {
		Pageable paging = PageRequest.of(pageNo, pageSize);
		Page<TransHistory> pagedResult = tRepository.findByUserIdPaging(userId, paging);
		return pagedResult.getContent();
	}

}
