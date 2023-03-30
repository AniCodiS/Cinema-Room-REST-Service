package cinema.Entity;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.UUID;

public class Purchase {

    private final UUID token;
    private final Seat ticket;

    public Purchase(Seat seat) {
        this.ticket = seat;
        this.token = UUID.randomUUID();
    }

    public String toJson() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }

    public UUID getToken() {
        return this.token;
    }

    public Seat getTicket() {
        return ticket;
    }
}
