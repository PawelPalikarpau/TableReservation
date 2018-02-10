package my.project.web.rest;

import my.project.entities.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReservationResourceTest {

    @Autowired
    TestRestTemplate testRestTemplate;

    //region [Tests For Work]

    @Test
    public void makeReservationTest() {
        LocalDate now = LocalDate.now().plusDays(1);
        Request dataToReserve = new Request(
                1,
                1,
                now.getYear(),
                now.getMonthValue(),
                now.getDayOfMonth(),
                14,
                14);
        RequestEntity requestEntity = new RequestEntity(null, dataToReserve, "reserve");

        ResponseEntity<Response> responseEntity =
                testRestTemplate.postForEntity("/reserve", requestEntity, Response.class);

        Response response = responseEntity.getBody();

        assertEquals("Reservation was successful", response.getMessage());
        assertEquals(true, response.getStatus());
    }

    @Test
    public void removeReservationTest() {
        LocalDate now = LocalDate.now().plusDays(1);
        Request dataToRemove = new Request(
                1,
                1,
                now.getYear(),
                now.getMonthValue(),
                now.getDayOfMonth(),
                20,
                20);
        RequestEntity requestEntity = new RequestEntity(dataToRemove, null, "remove");

        ResponseEntity<Response> responseEntity =
                testRestTemplate.postForEntity("/reserve", requestEntity, Response.class);

        Response response = responseEntity.getBody();

        assertEquals("Reservation was successfully removed", response.getMessage());
        assertEquals(true, response.getStatus());
    }

    @Test
    public void changeReservationTest() {
        LocalDate now = LocalDate.now().plusDays(1);
        Integer customerId = 1;

        Request dataToRemove = new Request(
                1,
                customerId,
                now.getYear(),
                now.getMonthValue(),
                now.getDayOfMonth(),
                14,
                14);

        Request dataToReserve = new Request(
                1,
                customerId,
                now.getYear(),
                now.getMonthValue(),
                now.getDayOfMonth(),
                19,
                20);
        RequestEntity requestEntity = new RequestEntity(dataToRemove, dataToReserve, "change");

        ResponseEntity<Response> responseEntity =
                testRestTemplate.postForEntity("/reserve", requestEntity, Response.class);

        Response response = responseEntity.getBody();

        assertEquals("Reservation was successfully changed", response.getMessage());
        assertEquals(true, response.getStatus());
    }

    //endregion

    //region [Checking Data Tests]

    @Test
    public void pastRemoveDateTest() {
        LocalDate now = LocalDate.now().minusDays(1);
        Request dataToRemove = new Request(2, 4, now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 14, 14);
        RequestEntity requestEntity = new RequestEntity(dataToRemove, null, "reserve");

        ResponseEntity<Response> responseEntity =
                testRestTemplate.postForEntity("/reserve", requestEntity, Response.class);

        Response response = responseEntity.getBody();

        assertEquals("In remove date: You can't add or remove reservation in past", response.getMessage());
        assertEquals(false, response.getStatus());
        assertEquals(null, response.getTableRemovedId());
        assertEquals(null, response.getTableReservedId());
    }

    @Test
    public void pastReserveDateTest() {
        LocalDate now = LocalDate.now().minusDays(1);
        Request dataToReserve = new Request(2, 4, now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 14, 14);
        RequestEntity requestEntity= new RequestEntity(null, dataToReserve, "reserve");

        ResponseEntity<Response> responseEntity =
                testRestTemplate.postForEntity("/reserve", requestEntity, Response.class);

        Response response = responseEntity.getBody();

        assertEquals("In reserve date: You can't add or remove reservation in past", response.getMessage());
        assertEquals(false, response.getStatus());
        assertEquals(null, response.getTableRemovedId());
        assertEquals(null, response.getTableReservedId());
    }

    @Test
    public void futureRemoveDateTest() {
        LocalDate now = LocalDate.now().plusMonths(1).plusDays(1);
        Request dataToRemove = new Request(2, 4, now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 14, 14);
        RequestEntity requestEntity= new RequestEntity(dataToRemove, null, "reserve");

        ResponseEntity<Response> responseEntity =
                testRestTemplate.postForEntity("/reserve", requestEntity, Response.class);

        Response response = responseEntity.getBody();

        assertEquals("In remove date: You can't make a reservation for more than a month", response.getMessage());
        assertEquals(false, response.getStatus());
        assertEquals(null, response.getTableRemovedId());
        assertEquals(null, response.getTableReservedId());
    }

    @Test
    public void futureReserveDateTest() {
        LocalDate now = LocalDate.now().plusMonths(1).plusDays(1);
        Request dataToReserve = new Request(2, 4, now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 14, 14);
        RequestEntity requestEntity= new RequestEntity(null, dataToReserve, "reserve");

        ResponseEntity<Response> responseEntity =
                testRestTemplate.postForEntity("/reserve", requestEntity, Response.class);

        Response response = responseEntity.getBody();

        assertEquals("In reserve date: You can't make a reservation for more than a month", response.getMessage());
        assertEquals(false, response.getStatus());
        assertEquals(null, response.getTableRemovedId());
        assertEquals(null, response.getTableReservedId());
    }

    @Test
    public void illegalReserveDateTest() {
        LocalDate now = LocalDate.now().plusDays(1);
        Request dataToReserve = new Request(2, 4, now.getYear(), 13, now.getDayOfMonth(), 14, 14);
        RequestEntity requestEntity= new RequestEntity(null, dataToReserve, "reserve");

        ResponseEntity<Response> responseEntity =
                testRestTemplate.postForEntity("/reserve", requestEntity, Response.class);

        Response response = responseEntity.getBody();

        assertEquals("In reserve date: This date doesn't exists in calendar", response.getMessage());
        assertEquals(false, response.getStatus());
        assertEquals(null, response.getTableRemovedId());
        assertEquals(null, response.getTableReservedId());
    }

    @Test
    public void illegalRemoveDateTest() {
        LocalDate now = LocalDate.now().plusDays(1);
        Request dataToRemove = new Request(2, 4, now.getYear(), 13, now.getDayOfMonth(), 14, 14);
        RequestEntity requestEntity= new RequestEntity(dataToRemove, null, "reserve");

        ResponseEntity<Response> responseEntity =
                testRestTemplate.postForEntity("/reserve", requestEntity, Response.class);

        Response response = responseEntity.getBody();

        assertEquals("In remove date: This date doesn't exists in calendar", response.getMessage());
        assertEquals(false, response.getStatus());
        assertEquals(null, response.getTableRemovedId());
        assertEquals(null, response.getTableReservedId());
    }

    @Test
    public void wrongTimeReserveDateTest() {
        LocalDate now =  LocalDate.now().plusDays(1);
        Request dataToReserve = new Request(2, 4, now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 10, 14);
        RequestEntity requestEntity= new RequestEntity(null, dataToReserve, "reserve");

        ResponseEntity<Response> responseEntity =
                testRestTemplate.postForEntity("/reserve", requestEntity, Response.class);

        Response response = responseEntity.getBody();

        assertEquals("In reserve date: Wrong time of reservation", response.getMessage());
        assertEquals(false, response.getStatus());
        assertEquals(null, response.getTableRemovedId());
        assertEquals(null, response.getTableReservedId());
    }

    @Test
    public void wrongTimeRemoveDateTest() {
        LocalDate now = LocalDate.now().plusDays(1);
        Request dataToRemove = new Request(2, 4, now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 10, 15);
        RequestEntity requestEntity= new RequestEntity(dataToRemove, null, "reserve");

        ResponseEntity<Response> responseEntity =
                testRestTemplate.postForEntity("/reserve", requestEntity, Response.class);

        Response response = responseEntity.getBody();

        assertEquals("In remove date: Wrong time of reservation", response.getMessage());
        assertEquals(false, response.getStatus());
        assertEquals(null, response.getTableRemovedId());
        assertEquals(null, response.getTableReservedId());
    }

    @Test
    public void maxTableCapacityReserveDateTest() {
        LocalDate now =  LocalDate.now().plusDays(1);
        Request dataToReserve = new Request(7, 4, now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 14, 14);
        RequestEntity requestEntity= new RequestEntity(null, dataToReserve, "reserve");

        ResponseEntity<Response> responseEntity =
                testRestTemplate.postForEntity("/reserve", requestEntity, Response.class);

        Response response = responseEntity.getBody();

        assertEquals("In reserve date: Maximum table capacity is six", response.getMessage());
        assertEquals(false, response.getStatus());
        assertEquals(null, response.getTableRemovedId());
        assertEquals(null, response.getTableReservedId());
    }

    @Test
    public void maxTableCapacityRemoveDateTest() {
        LocalDate now = LocalDate.now().plusDays(1);
        Request dataToRemove = new Request(7, 4, now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 14, 15);
        RequestEntity requestEntity= new RequestEntity(dataToRemove, null, "reserve");

        ResponseEntity<Response> responseEntity =
                testRestTemplate.postForEntity("/reserve", requestEntity, Response.class);

        Response response = responseEntity.getBody();

        assertEquals("In remove date: Maximum table capacity is six", response.getMessage());
        assertEquals(false, response.getStatus());
        assertEquals(null, response.getTableRemovedId());
        assertEquals(null, response.getTableReservedId());
    }

    @Test
    public void minTableCapacityReserveDateTest() {
        LocalDate now =  LocalDate.now().plusDays(1);
        Request dataToReserve = new Request(0, 4, now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 14, 14);
        RequestEntity requestEntity= new RequestEntity(null, dataToReserve, "reserve");

        ResponseEntity<Response> responseEntity =
                testRestTemplate.postForEntity("/reserve", requestEntity, Response.class);

        Response response = responseEntity.getBody();

        assertEquals("In reserve date: Minimum table capacity is one", response.getMessage());
        assertEquals(false, response.getStatus());
        assertEquals(null, response.getTableRemovedId());
        assertEquals(null, response.getTableReservedId());
    }

    @Test
    public void minTableCapacityRemoveDateTest() {
        LocalDate now = LocalDate.now().plusDays(1);
        Request dataToRemove = new Request(0, 4, now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 14, 15);
        RequestEntity requestEntity= new RequestEntity(dataToRemove, null, "reserve");

        ResponseEntity<Response> responseEntity =
                testRestTemplate.postForEntity("/reserve", requestEntity, Response.class);

        Response response = responseEntity.getBody();

        assertEquals("In remove date: Minimum table capacity is one", response.getMessage());
        assertEquals(false, response.getStatus());
        assertEquals(null, response.getTableRemovedId());
        assertEquals(null, response.getTableReservedId());
    }

    @Test
    public void twistedReserveDateTest() {
        LocalDate now =  LocalDate.now().plusDays(1);
        Request dataToReserve = new Request(2, 4, now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 15, 14);
        RequestEntity requestEntity= new RequestEntity(null, dataToReserve, "reserve");

        ResponseEntity<Response> responseEntity =
                testRestTemplate.postForEntity("/reserve", requestEntity, Response.class);

        Response response = responseEntity.getBody();

        assertEquals("In reserve date: Start and end dates are twisted", response.getMessage());
        assertEquals(false, response.getStatus());
        assertEquals(null, response.getTableRemovedId());
        assertEquals(null, response.getTableReservedId());
    }

    @Test
    public void twistedRemoveDateTest() {
        LocalDate now = LocalDate.now().plusDays(1);
        Request dataToRemove = new Request(2, 4, now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 15, 14);
        RequestEntity requestEntity= new RequestEntity(dataToRemove, null, "reserve");

        ResponseEntity<Response> responseEntity =
                testRestTemplate.postForEntity("/reserve", requestEntity, Response.class);

        Response response = responseEntity.getBody();

        assertEquals("In remove date: Start and end dates are twisted", response.getMessage());
        assertEquals(false, response.getStatus());
        assertEquals(null, response.getTableRemovedId());
        assertEquals(null, response.getTableReservedId());
    }

    @Test
    public void maxHoursReserveDateTest() {
        LocalDate now =  LocalDate.now().plusDays(1);
        Request dataToReserve = new Request(2, 4, now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 15, 17);
        RequestEntity requestEntity= new RequestEntity(null, dataToReserve, "reserve");

        ResponseEntity<Response> responseEntity =
                testRestTemplate.postForEntity("/reserve", requestEntity, Response.class);

        Response response = responseEntity.getBody();

        assertEquals("In reserve date: You can't reserve more than two hours", response.getMessage());
        assertEquals(false, response.getStatus());
        assertEquals(null, response.getTableRemovedId());
        assertEquals(null, response.getTableReservedId());
    }

    @Test
    public void maxHoursRemoveDateTest() {
        LocalDate now = LocalDate.now().plusDays(1);
        Request dataToRemove = new Request(2, 4, now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 15, 17);
        RequestEntity requestEntity= new RequestEntity(dataToRemove, null, "reserve");

        ResponseEntity<Response> responseEntity =
                testRestTemplate.postForEntity("/reserve", requestEntity, Response.class);

        Response response = responseEntity.getBody();

        assertEquals("In remove date: You can't reserve more than two hours", response.getMessage());
        assertEquals(false, response.getStatus());
        assertEquals(null, response.getTableRemovedId());
        assertEquals(null, response.getTableReservedId());
    }

    @Test
    public void emptyInputDataTest() {
        RequestEntity requestEntity= new RequestEntity(null, null, "reserve");

        ResponseEntity<Response> responseEntity =
                testRestTemplate.postForEntity("/reserve", requestEntity, Response.class);

        Response response = responseEntity.getBody();

        assertEquals("You haven't pass any data to operate", response.getMessage());
        assertEquals(false, response.getStatus());
        assertEquals(null, response.getTableRemovedId());
        assertEquals(null, response.getTableReservedId());
    }

    @Test
    public void wrongCommandTest() {
        LocalDate now = LocalDate.now().plusDays(1);
        Request dataToRemove = new Request(2, 4, now.getYear(), now.getMonthValue(), now.getDayOfMonth(), 15, 15);
        String command = "rrrrreserve";
        RequestEntity requestEntity= new RequestEntity(dataToRemove, null, command);

        ResponseEntity<Response> responseEntity =
                testRestTemplate.postForEntity("/reserve", requestEntity, Response.class);

        Response response = responseEntity.getBody();

        assertEquals("There is no such command like \"" + command + "\"", response.getMessage());
        assertEquals(false, response.getStatus());
        assertEquals(null, response.getTableRemovedId());
        assertEquals(null, response.getTableReservedId());
    }

    //endregion

}
