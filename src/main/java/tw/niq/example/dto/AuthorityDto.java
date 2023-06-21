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
public class AuthorityDto {

	private Long id;
	
	private String permission;
	
	@EqualsAndHashCode.Exclude
	private Set<RoleDto> roles;
	
}
