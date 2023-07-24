package tw.niq.example.model;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserModel {
	
	private Long id;
	
	private String password;

	private String username;
	
	@EqualsAndHashCode.Exclude
	private Set<RoleModel> roles;
	
	@EqualsAndHashCode.Exclude
	private Set<AuthorityModel> authorities;

	private Boolean accountNonExpired;

	private Boolean accountNonLocked;

	private Boolean credentialsNonExpired;

	private Boolean enabled = true;

}
