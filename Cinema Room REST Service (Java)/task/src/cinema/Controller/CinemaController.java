package cinema.Controller;

import cinema.Entity.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

@RestController
public class CinemaController {

    private Cinema newCinema;

    @GetMapping("/seats")
    public Cinema getAvailableSeats() {
        Cinema newCinema = new Cinema(9, 9);
        this.newCinema = newCinema;
        return newCinema;
    }

    @PostMapping("/purchase")
    public ResponseEntity<String> purchaseTicket(@RequestBody Seat requestSeat) throws IOException {
        int row = requestSeat.getRow();
        int column = requestSeat.getColumn();

        if (row > 9 || column > 9 || row < 1 || column < 1) {
            return ResponseEntity.status(400).body("{\"error\": \"The number of a row or a column is out of bounds!\"}");
        }

        ArrayList<Seat> availableSeats = newCinema.getAvailableSeats();
        boolean isAvailable = false;
        for (Seat seat : availableSeats) {
            if (seat.getRow() == row && seat.getColumn() == column) {
                isAvailable = true;
                seat.setAvailable(false);
                break;
            }
        }

        if (!isAvailable) {
            String jsonString = "{\"error\": \"The ticket has been already purchased!\"}";
            return ResponseEntity.status(400).body(jsonString);
        }

        int prevTicketNum = this.newCinema.getPurchasedTicketsNum();
        this.newCinema.setPurchasedTicketsNum(prevTicketNum + 1);
        int prevIncome = this.newCinema.getCurrentIncome();
        this.newCinema.setCurrentIncome(prevIncome + requestSeat.getPrice());
        int prevAvailableSeats = this.newCinema.getAvailableSeatsNum();
        this.newCinema.setAvailableSeatsNum(prevAvailableSeats - 1);

        Purchase newPurchase = new Purchase(requestSeat);
        this.newCinema.addToTokens(newPurchase.getToken(), requestSeat);
        return ResponseEntity.ok(newPurchase.toJson());
    }

    @PostMapping("/return")
    public ResponseEntity<String> returnTicket(@RequestBody Token token) throws IOException {
        Map<UUID, Seat> tokens = this.newCinema.getTokens();
        if (tokens.containsKey(token.getToken())) {
            Seat currentSeat = tokens.get(token.getToken());
            this.newCinema.removeFromToken(token.getToken());
            ArrayList<Seat> availableSeats = this.newCinema.getAvailableSeats();
            for (Seat seat : availableSeats) {
                if (seat.getRow() == currentSeat.getRow() && seat.getColumn() == currentSeat.getColumn()) {
                    seat.setAvailable(true);
                    break;
                }
            }

            int prevTicketNum = this.newCinema.getPurchasedTicketsNum();
            this.newCinema.setPurchasedTicketsNum(prevTicketNum - 1);
            int prevIncome = this.newCinema.getCurrentIncome();
            this.newCinema.setCurrentIncome(prevIncome - currentSeat.getPrice());
            int prevAvailableSeats = this.newCinema.getAvailableSeatsNum();
            this.newCinema.setAvailableSeatsNum(prevAvailableSeats + 1);

            ReturnedTicket returnedTicket = new ReturnedTicket(currentSeat);
            return ResponseEntity.ok(returnedTicket.toJson());
        } else {
            return ResponseEntity.status(400).body("{\"error\": \"Wrong token!\"}");
        }
    }

    @PostMapping("/stats")
    public ResponseEntity<String> getStatistics(@RequestParam
                                                (value="password", required=false) String password) throws IOException {
        if (password == null || !password.equals("super_secret")) {
            return ResponseEntity.status(401).body("{\"error\": \"The password is wrong!\"}");
        }
        int currentIncome = this.newCinema.getCurrentIncome();
        int purchasedTickets = this.newCinema.getPurchasedTicketsNum();
        int availableSeatsNum = this.newCinema.getAvailableSeatsNum();
        Statistics newStats = new Statistics(currentIncome, availableSeatsNum, purchasedTickets);
        return ResponseEntity.ok(newStats.toJson());
    }
}