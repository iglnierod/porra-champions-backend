package com.iglnierod.porra_champions.repository;

import com.iglnierod.porra_champions.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
