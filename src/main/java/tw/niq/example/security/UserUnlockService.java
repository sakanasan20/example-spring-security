package tw.niq.example.security;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tw.niq.example.entity.UserEntity;
import tw.niq.example.repository.UserRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserUnlockService {

	private final UserRepository userRepository;
	
	@Scheduled(fixedRate = 300000)
	public void unlockAccounts() {
		
		log.debug("Running Unlock Accounts");
		
		List<UserEntity> lockedUsers = 
				userRepository.findAllByAccountNonLockedAndLastModifiedDateIsBefore(false, 
						Timestamp.valueOf(LocalDateTime.now().minusSeconds(30)));
		
		if (lockedUsers.size() > 0) {
			log.debug("Locked Accounts Found, Unlocking");
			lockedUsers.forEach(lockUser -> lockUser.setAccountNonLocked(true));
			userRepository.saveAll(lockedUsers);
		}
	}
	
}
