package com.yifan.admin.api.model.baichuan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/11/15 17:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private String role;

    private String content;

}
