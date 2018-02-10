package my.project.entities;

public class RequestEntity {
    private Request dataToRemove;
    private Request dataToReserve;
    private String operation;

    public RequestEntity() {
    }

    public RequestEntity(Request dataToRemove, Request dataToReserve, String operation) {
        this.dataToRemove = dataToRemove;
        this.dataToReserve = dataToReserve;
        this.operation = operation;
    }

    public Request getDataToRemove() {
        return dataToRemove;
    }

    public void setDataToRemove(Request dataToRemove) {
        this.dataToRemove = dataToRemove;
    }

    public Request getDataToReserve() {
        return dataToReserve;
    }

    public void setDataToReserve(Request dataToReserve) {
        this.dataToReserve = dataToReserve;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
