package org.spring.dojooo.global.domain;

import jakarta.persistence.*;
import org.spring.dojooo.main.users.domain.User;

//사용자가 추가하고 삭제할 수있도록 설계 하기위해
@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @Column(nullable = false,length=10)
    private String tagName;

}
