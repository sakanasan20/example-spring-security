package tw.niq.example.dto;

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
public class RoleDto {
	
	private Long id;
	
	private String role;
	
	@EqualsAndHashCode.Exclude
	private Set<UserDto> users;
	
	@EqualsAndHashCode.Exclude
	private Set<AuthorityDto> authorities;

}
