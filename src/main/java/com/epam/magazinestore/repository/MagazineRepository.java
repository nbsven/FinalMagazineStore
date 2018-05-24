package com.epam.magazinestore.repository;

import com.epam.magazinestore.entity.Magazine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Performs basic CRUD operations
 */
@Repository
public interface MagazineRepository extends JpaRepository<Magazine, Long> {

  @Query("SELECT m FROM Magazine m WHERE NOT EXISTS (SELECT s.magazine FROM Subscription s where s.account.id = ?1 AND s.magazine = m)")
  Page<Magazine> getUnSubscribedMagazines(long accountId, Pageable pageable);

  @Query("SELECT m FROM Magazine m WHERE (LOWER(m.name) LIKE LOWER(CONCAT('%', ?1,'%')) " +
      "OR LOWER(m.publisher.name) LIKE LOWER(CONCAT('%', ?1,'%'))) AND (NOT EXISTS (SELECT s.magazine FROM Subscription s where s.account.id = ?2 AND s.magazine = m))")
  Page<Magazine> matchByMagazineNameOrPublisherName(String regex, Pageable pageable, long accountId);

}
