package org.spring.dojooo.main.users.repository;

import org.spring.dojooo.main.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email); //DB에 특정 이메일을 가진 사용자가 DB에 존재하는 지 확인.
}
