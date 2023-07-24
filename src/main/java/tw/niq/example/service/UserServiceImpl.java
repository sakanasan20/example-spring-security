package tw.niq.example.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import tw.niq.example.dto.UserDto;
import tw.niq.example.entity.UserEntity;
import tw.niq.example.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	
	@Transactional
	@Override
	public UserEntity loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException(username));
	}

	@Override
	public List<UserDto> readAllUsers() {
		
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		List<UserDto> users = userRepository.findAll().stream()
				.map(userEntity -> modelMapper.map(userEntity, UserDto.class))
				.collect(Collectors.toList());

		return users;
	}

	@Override
	public UserDto readUserByUserId(Long userId) {
		
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		UserEntity userEntity = userRepository.findById(userId).orElseThrow(()-> new RuntimeException());
		
		UserDto userDto = modelMapper.map(userEntity, UserDto.class);
		
		return userDto;
	}

	@Override
	public UserDto readUserByUserName(String username) {

		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		
		UserEntity userEntity = userRepository.findByUsernameSecured(username).orElseThrow(()-> new RuntimeException());
		
		UserDto userDto = modelMapper.map(userEntity, UserDto.class);
		
		return userDto;
	}

}
