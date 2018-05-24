package com.epam.magazinestore.services;

import com.epam.magazinestore.entity.Magazine;
import java.util.List;
import org.springframework.data.domain.Page;

/**
 * Service for working with magazines.
 */
public interface MagazineService {

  /**
   * Add {@code Magazine} to database, if magazine not exists.
   *
   * @param magazine {@code Magazine}, that needs to be added.
   * @return saved {@code Magazine} if adding was success, and {@code null} in other cases.
   */
  Magazine addMagazine(Magazine magazine);

  /**
   * Remove {@code Magazine} from database, if magazine exists.
   *
   * @param magazine {@code Magazine}, that needs to be removed.
   * @return {@code true} if deleting was success.
   */
  Magazine deleteMagazine(Magazine magazine);

  /**
   * Returns list of magazines available for subscription
   *
   * @param page zero-based page index
   * @param pageSize the size of the page to be returned
   * @return list of magazines available for subscription
   */
  List<Magazine> getAvailableMagazines(int page, int pageSize);

  Page<Magazine> getUnSubscribedMagazines(long accountId, int page, int pageSize, String direction);

  Magazine getMagazineById(long id);

  Magazine deleteMagazine(long id);

  Page<Magazine> getMagazines(int start, int length, String direction, String search, long accountId);
}
