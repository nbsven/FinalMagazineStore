package com.epam.magazinestore.services.impl;

import com.epam.magazinestore.entity.Magazine;
import com.epam.magazinestore.exception.ServiceException;
import com.epam.magazinestore.repository.MagazineRepository;
import com.epam.magazinestore.services.MagazineService;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MagazineServiceImpl implements MagazineService {

  private final MagazineRepository magazineRepository;

  @Override
  public Magazine addMagazine(@NonNull Magazine magazine) {
    Magazine savedMagazine;
    try {
      savedMagazine = magazineRepository.save(magazine);
    } catch (DataIntegrityViolationException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Could not save magazine: magazine with that name already exists", e);
    } catch (DataAccessException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Database error", e);
    }
    return savedMagazine;
  }

  @Override
  public Magazine deleteMagazine(@NonNull Magazine magazine) {
    try {
      magazineRepository.delete(magazine);
    } catch (EmptyResultDataAccessException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Could not delete magazine: magazine not found", e);
    } catch (InvalidDataAccessApiUsageException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Magazine can't be null", e);
    } catch (DataAccessException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Database error", e);
    }
    return magazine;
  }

  @Override
  public List<Magazine> getAvailableMagazines(int page, int pageSize) {
    Page<Magazine> magazinePage;
    if (page < 0 || pageSize < 0) {
      throw new ServiceException("Page index or page size can't be negative");
    }
    try {
      magazinePage = magazineRepository.findAll(new PageRequest(page, pageSize));
    } catch (DataAccessException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Database error", e);
    }
    return magazinePage.getContent();
  }

  @Override
  public Page<Magazine> getUnSubscribedMagazines(long accountId, int page, int pageSize, String direction) {
    Page<Magazine> magazinePage;
    Direction sortDirection = Direction.fromString(direction);
    if (page < 0 || pageSize < 0) {
      throw new ServiceException("Page index or page size can't be negative");
    }
    try {
      magazinePage = magazineRepository.getUnSubscribedMagazines(accountId, new PageRequest(page, pageSize,
          sortDirection, "price"));
    } catch (DataAccessException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Database error", e);
    }
    return magazinePage;
  }

  @Override
  public Magazine getMagazineById(long id) {
    Magazine magazine;
    try {
      magazine = magazineRepository.findOne(id);
    } catch (DataAccessException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Database error", e);
    }
    return magazine;
  }

  private Page<Magazine> search(long accountId, String match, int page, int pageSize, String direction) {
    Page<Magazine> magazines;
    Direction sortDirection = Direction.fromString(direction);
    try {
      magazines = magazineRepository.matchByMagazineNameOrPublisherName(match, new PageRequest(page, pageSize,
          sortDirection, "price"), accountId);
    } catch (DataAccessException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Database error", e);
    }
    return magazines;
  }

  @Override
  @Transactional
  public Magazine deleteMagazine(long id) {
    Magazine magazine;
    try {
      magazine = magazineRepository.findOne(id);
      magazineRepository.delete(id);
    } catch (EmptyResultDataAccessException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Could not delete magazine: magazine not found", e);
    } catch (InvalidDataAccessApiUsageException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Magazine can't be null", e);
    } catch (DataAccessException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Database error", e);
    }
    return magazine;
  }

  @Override
  public Page<Magazine> getMagazines(int start, int length,
                                     String direction, String search, long accountId) {
    Page<Magazine> magazines;
    int pageSize = getPageNumber(start, length);
    try {
      if (search.length() != 0) {
        magazines = search(accountId, search, pageSize, length, direction);
      } else {
        magazines =
            getUnSubscribedMagazines(accountId, pageSize, length, direction);
      }
    } catch (DataAccessException e) {
      log.error(e.getMessage(), e);
      throw new ServiceException("Database error", e);
    }
    return magazines;
  }

  private int getPageNumber(int start, int length) {
    int tmp;
    if (start < length) {
      tmp = start;
    } else {
      if ((start % length) == 0) {
        tmp = start / length;
      } else {
        tmp = start % length;
      }
    }
    return tmp;
  }

}
