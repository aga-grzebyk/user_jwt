package com.agagrzebyk.user_jwt.service;

import com.agagrzebyk.user_jwt.model.User;
import com.agagrzebyk.user_jwt.repo.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }



    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public User addUser(User user) {
        return this.userRepository.save(user);
    }

    public User findByUsername(@PathVariable String username){
        User foundUser = this.userRepository.findByUsername(username);
        if(foundUser == null){
            new EntityNotFoundException("nie ma w bazie");
        }
        return foundUser;
    }

    public User getUserById(Long id) {
        Optional<User> foundUser = this.userRepository.findById(id);
        User user = foundUser
                .orElseThrow(() -> new EntityNotFoundException
                        ("Could not find user with " + id + " id."));
        return user;
    }

    public User replaceUserById(User user, Long id) {
        Optional<User> optionalUser = this.userRepository.findById(id)
                .map(newUser -> {
                    newUser.setUsername(user.getUsername());
                    newUser.setFirstName(user.getFirstName());
                    newUser.setLastName(user.getLastName());
                    newUser.setEmail(user.getEmail());
                    newUser.setPassword(user.getPassword());
                    newUser.setIndexNumber(user.getIndexNumber());
                    newUser.setPermissions(user.getPermissions());
                    newUser.setRoles(user.getRoles());
                    return this.userRepository.save(newUser);
                });
        User updatedUser = optionalUser
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return updatedUser;
    }


    public void deleteUserById(Long id) {
        this.userRepository.deleteById(id);
    }
}
