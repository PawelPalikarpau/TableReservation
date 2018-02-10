package my.project.entities;

public class Response {
    private String message;
    private Boolean status;
    private Long tableRemovedId;
    private Long tableReservedId;

    public Response() { }

    public Response(String message, Boolean status, Long tableRemovedId, Long tableReservedId) {
        this.message = message;
        this.status = status;
        this.tableRemovedId = tableRemovedId;
        this.tableReservedId = tableReservedId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Long getTableRemovedId() {
        return tableRemovedId;
    }

    public void setTableRemovedId(Long tableRemovedId) {
        this.tableRemovedId = tableRemovedId;
    }

    public Long getTableReservedId() {
        return tableReservedId;
    }

    public void setTableReservedId(Long tableReservedId) {
        this.tableReservedId = tableReservedId;
    }
}
