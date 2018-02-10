package my.project.domain;

import javax.persistence.*;

@Entity
@Table(name = "table")
public class TableModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "capacity")
    private Integer capacity;

    public TableModel() {
    }

    public TableModel(Integer capacity) {
        this.capacity = capacity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}
