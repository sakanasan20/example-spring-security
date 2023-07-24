package tw.niq.example.dto;

import java.util.Set;
import java.util.stream.Collectors;

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
public class UserDto {
	
	private Long id;
	
	private String password;

	private String username;
	
	@EqualsAndHashCode.Exclude
	private Set<RoleDto> roles;
	
	@EqualsAndHashCode.Exclude
	private Set<AuthorityDto> authorities;

	private Boolean accountNonExpired;

	private Boolean accountNonLocked;

	private Boolean credentialsNonExpired;

	private Boolean enabled = true;
	
	public Set<AuthorityDto> getAuthorities() {
		return this.roles.stream().map(RoleDto::getAuthorities).flatMap(Set::stream).collect(Collectors.toSet());
	}

}
