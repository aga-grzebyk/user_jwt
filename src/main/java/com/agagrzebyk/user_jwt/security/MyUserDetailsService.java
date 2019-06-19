package com.agagrzebyk.user_jwt.security;

import com.agagrzebyk.user_jwt.model.User;
import com.agagrzebyk.user_jwt.repo.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        User user = this.userRepository.findByEmail(email);
        if(user == null){
            throw new UsernameNotFoundException("W bazie nie wystepuje użytkownik przypisany do podanego adresu email");
        }

        MyUserDetails userWithmyDetails = new MyUserDetails(user);
        return userWithmyDetails;
    }
}
