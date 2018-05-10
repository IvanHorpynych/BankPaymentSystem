package entity;

public abstract class Designation {
    private int id;
    private String name;

    public Designation() {
    }

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
        return getClass().getSimpleName() + "{" +
                "id=" + id +
                ", name= " + name +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Designation designation = (Designation) o;

        return id == designation.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
