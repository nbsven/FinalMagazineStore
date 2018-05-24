package com.epam.magazinestore.repository;

import com.epam.magazinestore.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Performs basic CRUD operations
 */
@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {

}
