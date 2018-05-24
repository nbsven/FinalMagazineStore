package com.epam.magazinestore.entity;

import com.epam.magazinestore.serializers.LocalDateToStringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * Represents payment data in a database table
 */
@Entity
@AllArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Table(name = "payments")
public class Payment {

  @Id
  @Column(name = "payment_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private long id;

  @Column(name = "date")
  @JsonSerialize(using = LocalDateToStringSerializer.class)
  private LocalDateTime date;

  @Column(name = "paid")
  private boolean paid;

  @Column(name = "amount")
  private int amount;

  @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST})
  @JoinColumn(name = "subscription_id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Subscription subscription;

  public Payment() {
    this(0);
  }

  public Payment(int amount) {
    this(amount, LocalDateTime.now());
  }

  public Payment(int amount, LocalDateTime date) {
    this.amount = amount;
    this.date = date;
    this.paid = false;
  }

}
