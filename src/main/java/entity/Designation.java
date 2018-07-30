package entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Designation {
  @Id
  protected int id;
  @Column
  protected String name;


  public Designation() {}

  ;

  public Designation(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "{" + "id=" + id + ", name= " + name + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    Designation designation = (Designation) o;

    return id == designation.id;
  }

  @Override
  public int hashCode() {
    return id;
  }
}
