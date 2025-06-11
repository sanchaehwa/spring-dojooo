package org.spring.dojooo.main.contents.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.spring.dojooo.main.users.domain.Profile;
import org.spring.dojooo.main.users.domain.User;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name="tech_log") //UserId 로 각 User의 메모를 구분
@NoArgsConstructor
@AllArgsConstructor

public class TechLog {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long techLogid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user; //사용자정보

    @Column(nullable = false, unique = true,length = 45)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content; //이미지 포함한 마크다운 형식으로 글 작성


    @Column(name="thumb_nail")
    private String thumbnailImageUrl;

    @Column(name = "content_image")
    private String contentImageUrl; //썸네일용

    @Column(nullable = false,columnDefinition = "TINYINT default 0")
    private boolean isDeleted; //삭제 여부

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false, columnDefinition = "TINYINT default 0")
    private boolean isPublic;  //boolean 자체가 null 안되니깐 nullable = false는 사실상 의미 없음 Boolean 으로 쓴다면 Null 허용이라 설정해줘야하고

    @PrePersist //memberRepository.save -DB에 저장되기 직전에 실행이 되서 자동 설정 - JPA 생명주기 이벤트 콜백
    public void time(){
        this.createdAt = LocalDateTime.now();
    }


    @Builder
    public TechLog(User user, String title, String content, String thumbnailImageUrl, String contentImageUrl, Boolean isDeleted, Boolean isPublic, LocalDateTime createdAt) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.thumbnailImageUrl = thumbnailImageUrl; //썸네일 기본이미지 Null이면
        this.contentImageUrl = contentImageUrl != null ? contentImageUrl : "";
        this.isDeleted = isDeleted != null ? isDeleted : false;
        this.isPublic = isPublic != null ? isPublic : false;
        this.createdAt = createdAt;
    }

    public void updateTechLogTitle(String title){
        this.title = title;
    }
    public void updateTechLogContent(String content){
        this.content = content;
    }
    public void changeIsPublic(boolean isPublic){
        this.isPublic = isPublic;
    }
    public void changeIsDeleted(boolean isDeleted){
        this.isDeleted = isDeleted;
    }
    public void updateTechLogThumbnailImageUrl(String thumbnailImageUrl){
        this.thumbnailImageUrl = thumbnailImageUrl;
    }


}
