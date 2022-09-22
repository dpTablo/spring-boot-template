package com.dptablo.template.springboot.repository;

import com.dptablo.template.springboot.model.entity.UserRole;
import com.dptablo.template.springboot.model.enumtype.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRoleRepositoryTest {
    @Autowired
    private UserRoleRepository userRoleRepository;

    @DisplayName("entity 저장 후 조회 테스트")
    @Test
    void saveAndFind() {
        //given
        UserRole userRole = UserRole.builder()
                .userId("dpTablo")
                .role(Role.ADMIN)
                .build();

        //when
        UserRole savedUserRole = userRoleRepository.save(userRole);

        UserRole foundUserRole = userRoleRepository.findById(savedUserRole.getSequence()).orElseThrow(NullPointerException::new);

        //then
        assertThat(foundUserRole).isEqualTo(savedUserRole);
    }
}