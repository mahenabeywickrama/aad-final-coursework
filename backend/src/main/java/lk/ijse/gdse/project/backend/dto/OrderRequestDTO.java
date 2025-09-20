package lk.ijse.gdse.project.backend.dto;

import java.util.List;

public class OrderRequestDTO {
    private List<CartItemDTO> items;
    private String username;

    public OrderRequestDTO() {
    }

    public OrderRequestDTO(List<CartItemDTO> items, String username) {
        this.items = items;
        this.username = username;
    }

    public List<CartItemDTO> getItems() {
        return items;
    }

    public void setItems(List<CartItemDTO> items) {
        this.items = items;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
