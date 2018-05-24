package com.epam.magazinestore.entity;

import com.epam.magazinestore.serializers.LocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.LocalDate;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

/**
 * Represents subscription data in a database table
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"account"})
@Getter
@Setter
@Table(name = "subscriptions")
@Builder
@JsonIgnoreProperties("payment")
public class Subscription {

  @Id
  @Column(name = "subscription_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private long id;

  @JsonSerialize(using = LocalDateSerializer.class)
  @Column(name = "start_date")
  private LocalDate startDate;

  @JsonSerialize(using = LocalDateSerializer.class)
  @Column(name = "end_date")
  private LocalDate endDate;

  @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "magazine_id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Magazine magazine;

  @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "account_id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Account account;

  @OneToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE,
      CascadeType.REMOVE}, fetch = FetchType.EAGER)
  @JoinColumn(name = "payment_id")
  private Payment payment;

}
