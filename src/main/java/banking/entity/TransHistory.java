package banking.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

@Entity
@Table(name="transhistory")
public class TransHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int transId;
	public int userId;
	public int amount;
	public LocalDateTime transTime;

	public int getTransId() {
		return transId;
	}

	public void setTransId(int transId) {
		this.transId = transId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public LocalDateTime getTransTime() {
		return transTime;
	}

	public void setTransTime(LocalDateTime transTime) {
		this.transTime = transTime;
	}

}
