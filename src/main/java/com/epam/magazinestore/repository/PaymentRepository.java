package com.epam.magazinestore.repository;

import com.epam.magazinestore.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Performs basic CRUD operations
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

  Page<Payment> findAllByPaidIsFalse(Pageable pageable);

  Page<Payment> findAllByPaidIsTrue(Pageable pageable);

  @Query("SELECT p FROM Payment p WHERE (LOWER(p.subscription.account.username) LIKE LOWER(CONCAT('%', ?1,'%'))) AND p.paid=TRUE")
  Page<Payment> matchPaidPaymentsByAccountName(String regex, Pageable pageable);

  @Query("SELECT p FROM Payment p WHERE (LOWER(p.subscription.account.username) LIKE LOWER(CONCAT('%', ?1,'%'))) AND p.paid=FALSE")
  Page<Payment> matchUnPaidPaymentsByAccountName(String regex, Pageable pageable);

}
