package org.spring.dojooo.main.users.domain;

import jakarta.persistence.*;
import lombok.*;
import org.spring.dojooo.global.ErrorCode;
import org.spring.dojooo.main.users.dto.UserUpdateRequest;
import org.spring.dojooo.main.users.exception.IllegalArgumentException;
import org.spring.dojooo.main.users.model.Role;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.*;
import java.util.stream.Collectors;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor

public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 45)
    private String nickname;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false)
    private String password;
//
//    @Enumerated(EnumType.STRING) //enum 값을 문자열로 저장
//    @Column(nullable = false)
//    private SocialType socialType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(columnDefinition = "TINYINT default 0")
    private boolean isDeleted;

    @Embedded
    private Profile profile;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "user")
    private List<ProfileTag> profileTags = new ArrayList<>();

    //프로필 이미지 등록하지않아도, 기본이미지가 보이게, 프로필 이미지 새로등록하면 기본이미지에서 바뀌는 로직으로
    private static final String DEFAULT_PROFILE_IMAGE = "https://dojooo.s3.ap-northeast-2.amazonaws.com/profile/80aefad7-3_기본프로필.jpg";



    @Builder
    public User(String nickname, String email,Boolean isDeleted, String password,Profile profile) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.role = Role.USER;
        this.isDeleted = isDeleted != null ? isDeleted : false;
        this.profile = new Profile(DEFAULT_PROFILE_IMAGE,null); //profileimage = null, introduction = null



    }
    //수정 가능 항목
    enum UpdateInfo {
        NICKNAME("nickname"),
        EMAIL("email");

        private String option;

        UpdateInfo(String option) {
            this.option = option;
        }
        public static UpdateInfo getUpdateOption(String input) {
            return Arrays
                    .stream(UpdateInfo.values())
                    .filter(user -> user.option.equals(input)) //Option 필드가 입력값과 일치하는지 필터링
                    .findFirst() //일치한 첫번째 값을 Optional
                    .orElseThrow(() -> new IllegalArgumentException(ErrorCode.INVALID_INPUT)); //예외처리
        }
    }


    //회원 정보 수정
    public void updateUser(UserUpdateRequest userUpdateRequest) {
        switch(UpdateInfo.getUpdateOption(userUpdateRequest.getOption())){
            case NICKNAME -> this.nickname = userUpdateRequest.getValue();
            case EMAIL -> this.email = userUpdateRequest.getValue();
        }
    }

    //비밀번호 암호화
    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }
    //회원 탈퇴 - 회원 삭제
    public void deleteUser(){
        this.isDeleted = true;
    }

    //프로필 업로드
    public void updateProfile(Profile profile) {
        this.profile = profile;
    }

    public List<String> getVisibleProfileTagNames() {
        return this.profileTags.stream()
                .filter(ProfileTag::isShowOnProfile)
                .map(pt -> pt.getTagName())
                .collect(Collectors.toList());
    }
    //편의매서드
    public void addProfileTag(ProfileTag tag) {
        this.profileTags.add(tag);
        // 양방향 연관관계 설정
        tag.setUserInternal(this); // private 메서드에서만 user 세팅
    }

}
