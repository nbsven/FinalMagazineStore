package com.epam.magazinestore.repository;

import com.epam.magazinestore.entity.Subscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Performs basic CRUD operations
 */
@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

  Page<Subscription> findAllByPaymentPaidIsFalseAndAccountId(long accountId, Pageable pageable);

  Page<Subscription> findAllByAccountId(long accountId, Pageable pageable);

  @Query(
      "SELECT s FROM Subscription s WHERE s.account.id = ?1 AND (LOWER(s.magazine.name) LIKE LOWER(CONCAT('%', ?2,'%')) "
          +
          "OR LOWER(s.magazine.publisher.name) LIKE LOWER(CONCAT('%', ?2,'%')))")
  Page<Subscription> matchByMagazineNameOrPublisherName(long accountId, String regex, Pageable pageable);
}
