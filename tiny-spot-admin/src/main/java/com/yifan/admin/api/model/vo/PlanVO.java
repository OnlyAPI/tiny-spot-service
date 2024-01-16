package com.yifan.admin.api.model.vo;

import com.yifan.admin.api.entity.Plan;
import com.yifan.admin.api.enums.StatusEnum;
import lombok.Data;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/11/17 11:27
 */
@Data
public class PlanVO {

    private Long id;

    private String text;

    private Boolean done;


    public PlanVO(Plan plan) {
        if (plan != null) {
            this.id = plan.getId();
            this.text = plan.getPlanDesc();
            this.done = plan.getStatus().equals(StatusEnum.COMPLETED.getStatus());
        }
    }
}
