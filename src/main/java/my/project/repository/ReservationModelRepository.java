package my.project.repository;

import my.project.domain.ReservationModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationModelRepository extends CrudRepository<ReservationModel, Long> {
    List<ReservationModel> findAllByTableIdAndYearAndMonthAndDayAndHoursBetween(Long tableId, Integer year, Integer month, Integer day, Integer startHours, Integer endHours);
    List<ReservationModel> findAllByCustomerIdAndYearAndMonthAndDayAndHoursBetween(Integer customerId, Integer year, Integer month, Integer day, Integer startHour, Integer endHour);
}
