package cinema.Entity;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ReturnedTicket {

    private Seat returned_ticket;

    public ReturnedTicket(Seat returned_ticket) {
        this.returned_ticket = returned_ticket;
    }

    public Seat getReturned_ticket() {
        return returned_ticket;
    }

    public void setReturned_ticket(Seat returned_ticket) {
        this.returned_ticket = returned_ticket;
    }

    public String toJson() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }
}
