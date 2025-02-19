package server.api.security;

import server.api.domain.Role;
import server.api.mapper.user.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import server.framework.custom.CustomMap;

import java.util.Collections;
import java.util.Map;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    public CustomUserDetailsService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Map<String, Object> userMap = fetchUserFormDatabase(username);

        // Map에서 데이터 추출
        String password = (String) userMap.get("password");
        String role = (String) userMap.get("role");

        return createUserDetails(username, password, role);
    }

    private CustomMap fetchUserFormDatabase(String username) {
        try {
            Map<String, Object> userMap = userMapper.findByUsername(username);
            if (userMap == null) {
                throw new UsernameNotFoundException("User not found: " + username);
            }

            return (CustomMap) userMap;
        } catch (Exception e) {
            throw new RuntimeException("Database error occurred while fetching user details", e);
        }
    }

    private UserDetails createUserDetails(String username, String password, String role) {
        if(role == null || role.trim().isEmpty()) {
            role = Role.USER.name();
        }

        return new org.springframework.security.core.userdetails.User(
                username,
                password,
                Collections.singleton(new SimpleGrantedAuthority(role))
        );
    }
}
