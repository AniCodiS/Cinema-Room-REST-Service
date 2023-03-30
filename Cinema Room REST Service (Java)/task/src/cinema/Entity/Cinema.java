package cinema.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Cinema {

    private int total_rows;
    private int total_columns;
    private Seat[] available_seats;
    @JsonIgnore
    private Map<UUID, Seat> tokens;
    @JsonIgnore
    private int currentIncome;
    @JsonIgnore
    private int purchasedTicketsNum;
    @JsonIgnore
    private int availableSeatsNum;

    public Cinema(int totalRows, int totalColumns) {
        this.total_rows = totalRows;
        this.total_columns = totalColumns;

        this.available_seats = new Seat[this.total_rows * this.total_columns];
        for (int i = 0; i < this.total_rows; i++) {
            for (int j = 0; j < this.total_columns; j++) {
                this.available_seats[i * this.total_rows + j] = new Seat(i + 1, j + 1);
            }
        }

        this.tokens = new HashMap<UUID, Seat>();
        this.currentIncome = 0;
        this.purchasedTicketsNum = 0;
        this.availableSeatsNum = this.total_rows * this.total_columns;
    }

    public int getTotalRows() {
        return total_rows;
    }

    public void setTotalRows(int total_rows) {
        this.total_rows = total_rows;
    }

    public int getTotalColumns() {
        return total_columns;
    }

    public void setTotalColumns(int total_columns) {
        this.total_columns = total_columns;
    }

    public ArrayList<Seat> getAvailableSeats() {
        ArrayList<Seat> available_seats = new ArrayList<>();
        for (Seat availableSeat : this.available_seats) {
            if (availableSeat.isAvailable()) {
                available_seats.add(availableSeat);
            }
        }
        return available_seats;
    }

    @JsonIgnore
    public int getAvailableSeatsNum() {
        return this.availableSeatsNum;
    }

    @JsonIgnore
    public void setAvailableSeatsNum(int newNum) {
        this.availableSeatsNum = newNum;
    }

    public void setAvailableSeats(Seat[] available_seats) {
        this.available_seats = available_seats;
    }

    public Map<UUID, Seat> getTokens() {
        return tokens;
    }

    public void addToTokens(UUID token, Seat newSeat) {
        this.tokens.put(token, newSeat);
    }

    public void removeFromToken(UUID token) {
        this.tokens.remove(token);
    }

    public int getCurrentIncome() {
        return currentIncome;
    }

    public void setCurrentIncome(int currentIncome) {
        this.currentIncome = currentIncome;
    }

    public int getPurchasedTicketsNum() {
        return purchasedTicketsNum;
    }

    public void setPurchasedTicketsNum(int purchasedNum) {
        this.purchasedTicketsNum = purchasedNum;
    }
}
