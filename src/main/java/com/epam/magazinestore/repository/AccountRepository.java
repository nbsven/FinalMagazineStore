package com.epam.magazinestore.repository;

import com.epam.magazinestore.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Performs basic CRUD operations
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

  boolean existsByUsername(String username);

  Account findByUsername(String username);
}
