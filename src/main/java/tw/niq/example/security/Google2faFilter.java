package tw.niq.example.security;

import java.io.IOException;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.autoconfigure.security.servlet.StaticResourceRequest;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import tw.niq.example.entity.UserEntity;

@Slf4j
@Component
public class Google2faFilter extends GenericFilterBean {
	
	private final AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();
	private final Google2faFailureHandler google2faFailureHandler = new Google2faFailureHandler();
	private final RequestMatcher urlIs2Fa = new AntPathRequestMatcher("/verify2fa");
	private final RequestMatcher urlResource = new AntPathRequestMatcher("/resources/**");

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		
		StaticResourceRequest.StaticResourceRequestMatcher staticResourceRequestMatcher = 
				PathRequest.toStaticResources().atCommonLocations();
		
		if (urlIs2Fa.matches(httpServletRequest) 
				|| urlResource.matches(httpServletRequest) 
				|| staticResourceRequestMatcher.matches(httpServletRequest)) {
			chain.doFilter(request, response);
			return;
		}
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication != null && !authenticationTrustResolver.isAnonymous(authentication)) {
			log.debug("Processing 2FA Filter");
			
			if (authentication.getPrincipal() != null && authentication.getPrincipal() instanceof UserEntity) {
				UserEntity user = (UserEntity) authentication.getPrincipal();
				
				if (user.getGoogle2faEnabled()) {
					log.debug("2FA Verification Reqiured");
					
					google2faFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, null);
					
					return;
				}
			}
		}
		
		chain.doFilter(request, response);
	}

}
