package banking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import banking.entity.TransHistory;

@Repository
public interface TransHistoryRepository extends JpaRepository<TransHistory,Integer> {
	@Query("SELECT t FROM TransHistory t WHERE t.userId=:userId")
	List<TransHistory> getTransHistory(int userId);
}
