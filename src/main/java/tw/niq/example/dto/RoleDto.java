package tw.niq.example.dto;

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
public class RoleDto {
	
	private Long id;
	
	private String role;
	
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private Set<UserDto> users;
	
	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	private Set<AuthorityDto> authorities;

}
