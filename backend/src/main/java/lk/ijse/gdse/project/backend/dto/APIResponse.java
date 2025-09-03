package lk.ijse.gdse.project.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class APIResponse {
    private int status;
    private String message;
    private Object data;
}
