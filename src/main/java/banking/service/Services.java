package banking.service;

import java.util.List;

import banking.model.TransHistory;
import banking.model.UserDto;

public interface Services {
	public UserDto deposit(int userId, int amount);

	public UserDto withdraw(int userId, int amount);

	public UserDto transfer(int userTransferId, int userBenefitId, int amount);

	public List<TransHistory> transHistoryByUserIdPaging(int userId, Integer pageNo, Integer pageSize);
}
