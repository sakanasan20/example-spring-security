package tw.niq.example.permission;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasAuthority('user.read')")
@Retention(RetentionPolicy.RUNTIME)
public @interface UserReadPermission {

}
