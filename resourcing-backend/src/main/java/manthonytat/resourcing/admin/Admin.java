package manthonytat.resourcing.admin;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import manthonytat.resourcing.user.User;

@Entity
@Table(name = "Admin")
public class Admin extends User {
  @Getter
  @Setter
  private String department;

  public Admin() {
  }

  public Admin(String department) {
    this.department = department;
  }
}
