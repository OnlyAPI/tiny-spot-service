package com.yifan.admin.api.model.byteDance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/11/27 11:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private String role;

    private String content;

}
