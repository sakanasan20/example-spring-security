package tw.niq.example.entity;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class UserEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String password;

	private String username;

	@Singular
	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(name = "user_authoriry", 
		joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") }, 
		inverseJoinColumns = { @JoinColumn(name = "authority_id", referencedColumnName = "id") })
	private Set<AuthorityEntity> authorities;

	@Builder.Default
	private Boolean accountNonExpired = true;

	@Builder.Default
	private Boolean accountNonLocked = true;

	@Builder.Default
	private Boolean credentialsNonExpired = true;

	@Builder.Default
	private Boolean enabled = true;

}
