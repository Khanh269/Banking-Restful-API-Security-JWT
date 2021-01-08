package banking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import banking.entity.Users;

public interface UsersRepository extends JpaRepository <Users,Integer>{
	Users findByuserName(String userName);
}
