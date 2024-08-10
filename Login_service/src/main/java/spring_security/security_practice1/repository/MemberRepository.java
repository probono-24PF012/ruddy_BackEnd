package spring_security.security_practice1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring_security.security_practice1.Entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

    boolean existsByUsername(String username);
}
