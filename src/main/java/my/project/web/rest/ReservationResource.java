package my.project.web.rest;

import my.project.entities.*;
import my.project.main.ReservationOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reserve")
public class ReservationResource {

    private final ReservationOperator reservationOperator;

    @Autowired
    public ReservationResource(ReservationOperator reservationOperator) {
        this.reservationOperator = reservationOperator;
    }

    @PostMapping
    public Response reserveTable(@RequestBody RequestEntity input) {
        return reservationOperator.chooseTheOperation(input);
    }
}