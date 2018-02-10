package my.project.main;

import my.project.entities.*;
import my.project.service.DatabaseOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ReservationOperator {

    private final DatabaseOperator databaseOperator;
    private String message;

    @Autowired
    public ReservationOperator(DatabaseOperator databaseOperator) {
        this.databaseOperator = databaseOperator;
    }

    public Response chooseTheOperation(RequestEntity requestEntity) {

        if (requestEntity.getDataToRemove() != null) {
            Response x = checkInputData(requestEntity.getDataToRemove(), "In remove date: ");
            if (x != null) return x;
        } else if (requestEntity.getDataToReserve() != null) {
            Response x = checkInputData(requestEntity.getDataToReserve(), "In reserve date: ");
            if (x != null) return x;
        } else if (requestEntity.getDataToReserve() == null && requestEntity.getDataToRemove() == null) {
            message = "You haven't pass any data to operate";
            return new Response(message, false, null, null);
        }

        databaseOperator.fillTablesInDatabase();

        if (requestEntity.getOperation().equalsIgnoreCase("reserve")) {
            return databaseOperator.reserve(requestEntity);
        } else if (requestEntity.getOperation().equalsIgnoreCase("remove")) {
            return databaseOperator.remove(requestEntity);
        } else if (requestEntity.getOperation().equalsIgnoreCase("change")) {
            return databaseOperator.changeReservation(requestEntity);
        } else {
            message = "There is no such command like \"" + requestEntity.getOperation() + "\"";
            return new Response(message, false, null, null);
        }
    }

    private Response checkInputData(Request inputData, String whichDate) {
        try {
            LocalDate date = LocalDate.of(inputData.getYear(), inputData.getMonth(), inputData.getDay());
            LocalDate now = LocalDate.now();

            if (now.compareTo(date) > 0) {
                message = whichDate + "You can't add or remove reservation in past";
                return new Response(message, false, null, null);
            }

            now = now.plusMonths(1L);
            if (now.compareTo(date) < 0) {
                message = whichDate + "You can't make a reservation for more than a month";
                return new Response(message, false, null, null);
            }
        } catch (Exception e) {
            message = whichDate + "This date doesn't exists in calendar";
            return new Response(message, false, null, null);
        }

        if (inputData.getStartHour() < 12 || inputData.getEndHour() > 23) {
            message = whichDate + "Wrong time of reservation";
            return new Response(message, false, null, null);
        } else if (inputData.getNumberOfPersons() > 6) {
            message = whichDate + "Maximum table capacity is six";
            return new Response(message, false, null, null);
        } else if (inputData.getNumberOfPersons() < 1) {
            message = whichDate + "Minimum table capacity is one";
            return new Response(message, false, null, null);
        } else if (inputData.getStartHour() > inputData.getEndHour()) {
            message = whichDate + "Start and end dates are twisted";
            return new Response(message, false, null, null);
        } else if (inputData.getEndHour() - inputData.getStartHour() + 1 > 2) {
            message = whichDate + "You can't reserve more than two hours";
            return new Response(message, false, null, null);
        }

        return null;
    }
}