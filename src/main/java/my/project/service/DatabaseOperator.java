package my.project.service;

import my.project.domain.*;
import my.project.entities.*;
import my.project.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DatabaseOperator {

    private final ReservationModelRepository reservationRepository;
    private final TableModelRepository tableRepository;

    private Request inputDataToAdd;
    private Request inputDataToRemove;

    private List<ReservationModel> reservationsToAdd;
    private List<ReservationModel> reservationsToRemove;

    private List<TableModel> tablesByCapacity;
    private Boolean changeReservation = false;
    private String message;

    @Autowired
    public DatabaseOperator(ReservationModelRepository reservationRepository, TableModelRepository tableRepository) {
        this.reservationRepository = reservationRepository;
        this.tableRepository = tableRepository;
    }

    public Response reserve(RequestEntity requestEntity) {
        inputDataToAdd = requestEntity.getDataToReserve();
        reservationsToAdd = getReservationsToReserve();

        Response responseReserve = findTableForReservation();
        if (!responseReserve.getStatus()) {
            return responseReserve;
        } else {

            Long tableIdToRemove = null;
            if (changeReservation) {
                Response responseRemove = remove(requestEntity);
                if (!responseRemove.getStatus()) {
                    return responseRemove;
                } else {
                    tableIdToRemove = responseRemove.getTableRemovedId();
                    reservationRepository.delete(reservationsToRemove);
                }
            }

            Integer numberOfHours = inputDataToAdd.getEndHour() - inputDataToAdd.getStartHour() + 1;
            return makeReservation(numberOfHours, tableIdToRemove, responseReserve.getTableReservedId());
        }
    }

    public Response remove(RequestEntity requestEntity) {
        inputDataToRemove = requestEntity.getDataToRemove();
        reservationsToRemove = getReservationsToRemove();

        Response response =  checkBeforeDelete(reservationsToRemove);
        if (!response.getStatus()) {
            return response;
        } else {

            if (!changeReservation) {
                reservationRepository.delete(reservationsToRemove);
            }

            message = "Reservation was successfully removed";
            return new Response(message, true, reservationsToRemove.get(0). getTableId(), null);
        }
    }

    public Response changeReservation(RequestEntity requestEntity) {
        changeReservation = true;
        return reserve(requestEntity);
    }

    private Response findTableForReservation() {
        Integer numberOfHours = inputDataToAdd.getEndHour() - inputDataToAdd.getStartHour() + 1;
        if (reservationsToAdd.size() == 0) {
            return new Response("", true, null, tablesByCapacity.get(0).getId());
        } else if (reservationsToAdd.size() == 1) {
            Long tableId = findRightTableId(reservationsToAdd.get(0).getTableId());
            return new Response("", true, null, tableId);
        } else if (reservationsToAdd.size() == 2 && numberOfHours > 1) {
            return foundTwoReservations();
        } else {
            message = "There is no free table to reserve at this time";
            return new Response(message, false, null, null);
        }
    }

    private Response foundTwoReservations() {
        Long firstId = reservationsToAdd.get(0).getTableId();
        Long secondId = reservationsToAdd.get(1).getTableId();

        if (firstId.equals(secondId)) {
            Long tableId = findRightTableId(firstId);
            return new Response("", true, null, tableId);
        } else {
            message = "There is no free table to reserve at this time";
            return new Response(message, false, null, null);
        }
    }

    private Long findRightTableId(Long wrongId) {
        return tablesByCapacity.stream()
                .filter(item -> !wrongId.equals(item.getId()))
                .map(TableModel::getId)
                .findAny()
                .orElse(null);
    }

    private Response checkBeforeDelete(List<ReservationModel> reservationsToDelete) {
        Integer numberOfHours = inputDataToRemove.getEndHour() - inputDataToRemove.getStartHour() + 1;

        if (reservationsToDelete.size() < 1) {
            message = "You have no reservations at this time";
            return new Response(message, false, null, null);
        } else if (reservationsToDelete.size() == 1 && numberOfHours == 2) {
            message = "You'v got reservation only for one hour at this time";
            return new Response(message, false, null , null);
        }

        return new Response("", true, null, null);
    }

    private List<ReservationModel> getReservationsToReserve() {
        Integer number = 0;
        if (inputDataToAdd.getNumberOfPersons() < 3) {
            number = 2;
        } else if (inputDataToAdd.getNumberOfPersons() > 2 && inputDataToAdd.getNumberOfPersons() < 5) {
            number = 4;
        } else if (inputDataToAdd.getNumberOfPersons() > 4) {
            number = 6;
        }
        tablesByCapacity = tableRepository.findAllByCapacity(number);
        List<ReservationModel> reservationList = new ArrayList<>();

        for (TableModel tableModel : tablesByCapacity) {
            reservationList.addAll(reservationRepository
                    .findAllByTableIdAndYearAndMonthAndDayAndHoursBetween(
                            tableModel.getId(),
                            inputDataToAdd.getYear(),
                            inputDataToAdd.getMonth(),
                            inputDataToAdd.getDay(),
                            inputDataToAdd.getStartHour(),
                            inputDataToAdd.getEndHour()
                    )
            );
        }

        return reservationList;
    }

    private List<ReservationModel> getReservationsToRemove() {
        return reservationRepository
                .findAllByCustomerIdAndYearAndMonthAndDayAndHoursBetween(
                        inputDataToRemove.getCustomerId(),
                        inputDataToRemove.getYear(),
                        inputDataToRemove.getMonth(),
                        inputDataToRemove.getDay(),
                        inputDataToRemove.getStartHour(),
                        inputDataToRemove.getEndHour()
        );
    }

    private ReservationModel getReservationModel(Long tableId, Integer hour) {
        ReservationModel reservationModel = new ReservationModel();
        reservationModel.setYear(inputDataToAdd.getYear());
        reservationModel.setMonth(inputDataToAdd.getMonth());
        reservationModel.setDay(inputDataToAdd.getDay());
        reservationModel.setHours(hour);
        reservationModel.setCustomerId(inputDataToAdd.getCustomerId());
        reservationModel.setTableId(tableId);
        return reservationModel;
    }

    private Response makeReservation(Integer numberOfHours, Long tableRemovedId, Long tableReservedId) {
        for (int i = 0; i < numberOfHours; i++) {
            ReservationModel model = getReservationModel(tableReservedId, inputDataToAdd.getStartHour() + i);
            reservationRepository.save(model);
        }
        if (!changeReservation) {
            return new Response("Reservation was successful", true, tableRemovedId, tableReservedId);
        } else {
            return new Response("Reservation was successfully changed", true, tableRemovedId, tableReservedId);
        }
    }

    public void fillTablesInDatabase() {
        if (tableRepository.count() < 1) {
            tableRepository.save(new TableModel(2));
            tableRepository.save(new TableModel(2));
            tableRepository.save(new TableModel(4));
            tableRepository.save(new TableModel(4));
            tableRepository.save(new TableModel(6));
            tableRepository.save(new TableModel(6));
        }
    }
}

/*
select * from table;
select * from reservation;
 */


/*
{
  "dataToRemove":{
    "numberOfPersons":2,
    "customerId":3,
    "year":2018,
    "month":2,
    "day":15,
    "startHour":14,
    "endHour":15
  },
    "dataToReserve":{
    "numberOfPersons":2,
    "customerId":3,
    "year":2018,
    "month":2,
    "day":15,
    "startHour":14,
    "endHour":15
  },
  "operation":"reserve"
}
*/