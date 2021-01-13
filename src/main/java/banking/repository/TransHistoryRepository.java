package banking.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import banking.entity.TransHistory;

public interface TransHistoryRepository extends JpaRepository<TransHistory,Integer>  {
	@Query("SELECT t FROM TransHistory t WHERE t.user.userId=:userId")
	Page<TransHistory> findById(int userId, Pageable pageable);
}
