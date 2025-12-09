package com.teamteskboard;

import com.teamteskboard.user.entity.User;
import com.teamteskboard.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataInitializer(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        // 유저 없으면 생성
        if (userRepository.count() == 0) {
            User u1 = new User("user1", "김주남", "user1@test.com", "1234", "ADMIN");
            User u2 = new User("user2", "김낑깡", "user2@test.com", "1234", "USER");
            userRepository.saveAll(List.of(u1, u2));
        }
    }
}
