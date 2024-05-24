package com.kerem.Repository;

import com.kerem.Entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth, Long> {
    Boolean existsByUsername(String username);

    Optional<Auth> findOptionalByUsernameAndPassword(String username, String password);

    //aktivasyon kodu ile hesap bulma
    Optional<Auth> findByActivationCode(String code);

    Optional<Auth> findByEmail(String email);
}
