package org.spring.dojooo.Image.domain;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.spring.dojooo.Image.model.TechLogImageType;
import org.spring.dojooo.main.contents.domain.TechLog;
import org.spring.dojooo.main.users.domain.User;

@Entity
@Table(name = "techlog_image")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TechLogImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "techlog_image_id")
    private Long id;

    @Column(nullable = false)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TechLogImageType imageType; // "thumbnail" 또는 "content"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "techlog_id")
    private TechLog techLog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
