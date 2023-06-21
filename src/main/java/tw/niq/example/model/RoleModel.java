package tw.niq.example.model;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleModel {
	
	private Long id;
	
	private String role;
	
	@EqualsAndHashCode.Exclude
	private Set<UserModel> users;
	
	@EqualsAndHashCode.Exclude
	private Set<AuthorityModel> authorities;

}
