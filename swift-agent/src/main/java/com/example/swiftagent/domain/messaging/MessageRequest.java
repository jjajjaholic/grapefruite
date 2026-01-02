package com.example.swiftagent.domain.messaging;

import lombok.Data;

@Data
public class MessageRequest {
    private String content;
    private String receiver;

}
