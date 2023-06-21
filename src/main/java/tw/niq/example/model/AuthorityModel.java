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
public class AuthorityModel {

	private Long id;
	
	private String permission;
	
	@EqualsAndHashCode.Exclude
	private Set<RoleModel> roles;
	
}
