package com.uniso.equso.dao.entities;

import com.uniso.equso.dao.enums.Status;
import com.uniso.equso.dao.enums.UserSubType;
import com.uniso.equso.dao.enums.UserType;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user", schema = "public")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private UserType type;

    @Enumerated(value = EnumType.STRING)
    private UserSubType subType;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(unique = true)
    private String alias;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean isAnonymous;

    private String about;

    @CreationTimestamp
    @Column(name = "created_at",updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "last_updated_at")
    private LocalDateTime lastUpdatedAt;

    @Enumerated(value = EnumType.STRING)
    private Status status;
}
