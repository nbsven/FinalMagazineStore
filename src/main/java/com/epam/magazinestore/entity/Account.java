package com.epam.magazinestore.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents account data in a database table
 */
@Entity
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"subscriptions"})
@AllArgsConstructor
@Getter
@Setter
@Table(name = "accounts")
@JsonIgnoreProperties("subscriptions")
@Builder
public class Account {

  @Id
  @Column(name = "account_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private long id;

  @Column(name = "username", unique = true, length = 20)
  private String username;

  @Column(name = "password", length = 20)
  private String password;

  @Column(name = "role", length = 10)
  @Enumerated(EnumType.STRING)
  private Role role;

  @OneToMany(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE,
      CascadeType.REMOVE}, fetch = FetchType.LAZY)
  @JoinColumn(name = "account_id")
  private List<Subscription> subscriptions;

  public void addSubscription(Subscription subscription) {
    this.subscriptions.add(subscription);
    if (subscription.getAccount() != this) {
      subscription.setAccount(this);
    }
  }

  public void removeSubscription(Subscription subscription) {
    subscriptions.remove(subscription);
    subscription.setAccount(null);
  }

}
