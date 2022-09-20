package com.dptablo.template.springboot.repository;

import com.dptablo.template.springboot.model.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @DisplayName("entity 저장 후 조회 테스트")
    @Test
    void saveAndFind() {
        //given
        User user = User.builder()
                .userId("dpTablo")
                .password("leeyw2355@gmail.com")
                .phoneNumber("01011112222")
                .build();

        //when
        User savedUser = userRepository.save(user);

        User foundUser = userRepository.findById(user.getUserId()).orElseThrow(NullPointerException::new);

        //then
        assertThat(foundUser).isEqualTo(savedUser);
    }
}