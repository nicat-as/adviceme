package com.uniso.equso.dao.entities;

import com.uniso.equso.dao.enums.Status;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "post")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(nullable = false)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private UserEntity creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wall_user_id")
    private UserEntity wallUser;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_category_id")
    private CategoryEntity category;

    @OneToMany(mappedBy = "post",
        orphanRemoval = true,
        cascade = CascadeType.ALL
    )
    List<CommentEntity> comments = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at",updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "last_updated_at")
    private LocalDateTime lastUpdatedAt;

    @Enumerated(value = EnumType.STRING)
    private Status status;
}
