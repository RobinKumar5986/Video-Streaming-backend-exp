package com.kgjr.videoStreaming.payload;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomMessage {

    private String message;

    private boolean success = false;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
