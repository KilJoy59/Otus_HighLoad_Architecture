package ru.otus.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.repository.user.UserRepository;
import ru.otus.util.dto.UserSecurityDto;

@Service
@RequiredArgsConstructor
public class JwtUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return null;
    }

    public UserSecurityDto getUserByIdUser(Long idUser) {
        UserSecurityDto user = userRepository.checkExistsUserAndGetUser(idUser);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid userId or user not found.");
        }

        return user;
    }

}
