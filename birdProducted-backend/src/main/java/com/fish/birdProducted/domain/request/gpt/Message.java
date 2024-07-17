package com.fish.birdProducted.domain.request.gpt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fish
 * <p>
 * 创建时间：2024/3/10 14:14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    String role;
    String content;
}
