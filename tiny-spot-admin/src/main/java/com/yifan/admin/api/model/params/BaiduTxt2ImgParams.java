package com.yifan.admin.api.model.params;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/11/20 17:59
 */
@Data
public class BaiduTxt2ImgParams {

    @NotBlank(message = "提示词不能为空")
    @Length(max = 1024, message = "提示词长度不能超过1024字符")
    private String prompt;

    @NotNull(message = "生成数量不能为空")
    @Range(min = 1, max = 4, message = "生成数量必须在1-10之间")
    private Integer generateNum;

    private String imageSize;

}
