package com.agagrzebyk.user_jwt.repo;

import com.agagrzebyk.user_jwt.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class InitDB implements CommandLineRunner {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public InitDB(UserRepository userRepository,
                  PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        User teacher = new User("teacher", "teacher@java.dev", passwordEncoder.encode("teacher123"),"TEACHER","");
        User admin = new User("admin", "admin@java.dev", passwordEncoder.encode("admin123"),"ADMIN","ACCESS_TEST1,ACCESS_TEST2");
        User student1 = new User("student1", "student1@java.dev", passwordEncoder.encode("student123"),"STUDENT","ACCESS_TEST1");
        User student2 = new User("student2", "student2@java.dev", passwordEncoder.encode("student234"),"STUDENT","ACCESS_TEST1");

        List<User> users = Arrays.asList(teacher,admin,student1,student2);

        // Save to db
        this.userRepository.saveAll(users);

    }
}
