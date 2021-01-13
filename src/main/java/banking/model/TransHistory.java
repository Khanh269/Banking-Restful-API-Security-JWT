package banking.model;

import java.time.LocalDateTime;

import javax.persistence.*;


@Entity
@Table(name = "transhistory")
public class TransHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "trans_id")
	public int transId;
	@Column
	public int amount;
	@Column
	public LocalDateTime transTime;
	@ManyToOne
	@JoinColumn(name = "user_id")
	public Users user;

	public int getTransId() {
		return transId;
	}

	public void setTransId(int transId) {
		this.transId = transId;
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

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

}
