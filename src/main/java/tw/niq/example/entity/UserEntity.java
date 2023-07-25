package tw.niq.example.entity;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
public class UserEntity implements UserDetails, CredentialsContainer {
	
	private static final long serialVersionUID = -7388782428110747282L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String password;

	private String username;

	@Builder.Default
	private Boolean accountNonExpired = true;

	@Builder.Default
	private Boolean accountNonLocked = true;

	@Builder.Default
	private Boolean credentialsNonExpired = true;

	@Builder.Default
	private Boolean enabled = true;
	
	@Builder.Default
	private Boolean google2faEnabled = false;
	
	private String google2faSecret;
	
	@Builder.Default
	@Transient
	private Boolean google2faNonVerified = true;
	
	@CreationTimestamp
	@Column(updatable = false)
	private Timestamp createdDate;
	
	@UpdateTimestamp
	private Timestamp lastModifiedDate;
	
	@Singular
	@ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", 
		joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") }, 
		inverseJoinColumns = { @JoinColumn(name = "role_id", referencedColumnName = "id") })
	private Set<RoleEntity> roles;
	
	@Transient
	public Set<GrantedAuthority> getAuthorities() {

		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		
		if (roles != null && roles.size() > 0) {
			grantedAuthorities.addAll(roles.stream()
					.map(RoleEntity::getRole)
					.map(role -> "ROLE_" + role)
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toSet()));
		}
		
		Set<GrantedAuthority> authorities = this.roles.stream()
				.map(RoleEntity::getAuthorities)
				.flatMap(Set::stream)
				.map(AuthorityEntity::getPermission)
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toSet());
		
		if (authorities != null && authorities.size() > 0) {
			grantedAuthorities.addAll(authorities);
		}
		
		return grantedAuthorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	@Override
	public void eraseCredentials() {
		this.password = null;
	}

}
