package my.project.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "reservation")
public class ReservationModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "year")
    private Integer year;

    @Column(name = "month")
    private Integer month;

    @Column(name = "day")
    private Integer day;

    @Column(name = "hours")
    private Integer hours;

    @Column(name = "table_id")
    private Long tableId;

    public ReservationModel() {
    }

    public ReservationModel(Integer customerId, Integer year, Integer month, Integer day, Integer hours, Long tableId) {
        this.customerId = customerId;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hours = hours;
        this.tableId = tableId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }
}
