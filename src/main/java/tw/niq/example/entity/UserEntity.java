package tw.niq.example.entity;

import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Transient;
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
	@ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", 
		joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") }, 
		inverseJoinColumns = { @JoinColumn(name = "role_id", referencedColumnName = "id") })
	private Set<RoleEntity> roles;
	
	@Transient
	private Set<AuthorityEntity> authorities;
	
	public Set<AuthorityEntity> getAuthorities() {
		return this.roles.stream()
				.map(RoleEntity::getAuthorities)
				.flatMap(Set::stream)
				.collect(Collectors.toSet());
	}

	@Builder.Default
	private Boolean accountNonExpired = true;

	@Builder.Default
	private Boolean accountNonLocked = true;

	@Builder.Default
	private Boolean credentialsNonExpired = true;

	@Builder.Default
	private Boolean enabled = true;

}
