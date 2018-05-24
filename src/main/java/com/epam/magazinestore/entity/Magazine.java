package com.epam.magazinestore.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents magazine data in a database table
 */
@Entity
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Setter
@Getter
@ToString
@Table(name = "magazines")
public class Magazine {

  @Id
  @Column(name = "magazine_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private long id;

  @Column(name = "name", length = 20, unique = true)
  private String name;

  @Column(name = "publication_date")
  private int publicationDate;

  @Column(name = "price")
  private int price;

  @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "publisher_id")
  private Publisher publisher;

}
