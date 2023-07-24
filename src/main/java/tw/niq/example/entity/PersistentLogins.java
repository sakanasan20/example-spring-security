package tw.niq.example.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class PersistentLogins {
	
	@Id
	private String series;

	@Column(nullable = false)
	private String username;
	
	@Column(nullable = false)
	private String token;
	
	@Column(nullable = false)
	private Timestamp lastUsed;
	
}
