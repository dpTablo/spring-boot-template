package com.dptablo.template.springboot.model.entity;

import com.dptablo.template.springboot.model.enumtype.Role;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "\"user_role\"")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@EqualsAndHashCode
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "user_role_sequence")
    private Long sequence;

    @Column(name = "user_id")
    private String userId;

    @Column
    private Role role;
}
