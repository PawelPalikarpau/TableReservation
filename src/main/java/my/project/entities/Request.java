package my.project.entities;

public class Request {

    private Integer numberOfPersons;
    private Integer customerId;
    private Integer year;
    private Integer month;
    private Integer day;
    private Integer startHour;
    private Integer endHour;

    public Request() {
    }

    public Request(Integer numberOfPersons, Integer customerId, Integer year, Integer month, Integer day, Integer startHour, Integer endHour) {
        this.numberOfPersons = numberOfPersons;
        this.customerId = customerId;
        this.year = year;
        this.month = month;
        this.day = day;
        this.startHour = startHour;
        this.endHour = endHour;
    }

    public Integer getNumberOfPersons() {
        return numberOfPersons;
    }

    public void setNumberOfPersons(Integer numberOfPersons) {
        this.numberOfPersons = numberOfPersons;
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

    public Integer getStartHour() {
        return startHour;
    }

    public void setStartHour(Integer startHour) {
        this.startHour = startHour;
    }

    public Integer getEndHour() {
        return endHour;
    }

    public void setEndHour(Integer endHour) {
        this.endHour = endHour;
    }
}
