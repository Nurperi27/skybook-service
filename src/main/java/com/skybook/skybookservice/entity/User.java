package com.skybook.skybookservice.entity;

import com.skybook.skybookservice.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_id_seq", allocationSize = 1)
    Long id;
    @Column(nullable = false)
    String name;
    @Column(nullable = false, unique = true)
    String email;
    @Column(nullable = false)
    String password;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    UserRole role;
    @Column(name = "is_active", nullable = false)
    Boolean isActive;
    @Column(name = "created_at", nullable = false, updatable = false)
    LocalDateTime createdAt;

    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
        if(this.isActive == null){
            this.isActive = true;
        }
    }
}
