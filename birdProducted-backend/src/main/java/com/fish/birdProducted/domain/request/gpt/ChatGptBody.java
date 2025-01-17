package com.fish.birdProducted.domain.request.gpt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author fish
 * <p>
 * 创建时间：2024/3/10 14:13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatGptBody {
    String model;
    boolean stream;
    List<Message> messages;
}
