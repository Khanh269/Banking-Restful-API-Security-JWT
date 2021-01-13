package banking.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import banking.model.Users;

public interface UsersRepository extends JpaRepository <Users,Integer>{
	Users findByuserName(String userName);
	@Query("select u from Users u")
	List<Users> findUsersPaging (Pageable pageable);
}
