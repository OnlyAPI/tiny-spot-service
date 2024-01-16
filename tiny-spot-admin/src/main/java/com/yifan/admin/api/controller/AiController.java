package com.yifan.admin.api.controller;

import com.yifan.admin.api.entity.AiConversationHistory;
import com.yifan.admin.api.model.params.CreateAiConversationParams;
import com.yifan.admin.api.model.vo.AiRobotVO;
import com.yifan.admin.api.model.vo.ConversationHistoryVO;
import com.yifan.admin.api.model.vo.ConversationVO;
import com.yifan.admin.api.result.BaseResult;
import com.yifan.admin.api.service.AiConversationHistoryService;
import com.yifan.admin.api.service.AiConversationService;
import com.yifan.admin.api.service.AiRobotService;
import com.yifan.admin.api.utils.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author TaiYi
 * @ClassName
 * @date 2023/12/8 15:23
 */
@Slf4j
@RestController
@RequestMapping("/ai")
public class AiController {

    @Resource
    private AiRobotService aiRobotService;
    @Resource
    private AiConversationService aiConversationService;
    @Resource
    private AiConversationHistoryService aiConversationHistoryService;

    @GetMapping("/robots")
    public BaseResult<List<AiRobotVO>> getAiRobotList(@RequestParam(value = "name", required = false, defaultValue = "") String name) {
        return BaseResult.ok(aiRobotService.getAiRobotList(name));
    }


    @PostMapping("/conversation")
    public BaseResult<String> createConversation(@RequestBody @Valid CreateAiConversationParams params) {
        return BaseResult.ok(aiConversationService.createAiConversation(params));
    }

    @GetMapping("/conversation/list")
    public BaseResult<List<ConversationVO>> getAiConversationList() {
        return BaseResult.ok(aiConversationService.getAiConversationList());
    }

    @GetMapping("/conversation/{cid}")
    public BaseResult<ConversationVO> getConversationInfoByCId(@PathVariable(value = "cid") String cid) {
        return BaseResult.ok(aiConversationService.getConversationInfoByCid(cid));
    }

    @DeleteMapping("/conversation/{cid}")
    public BaseResult<Void> delConversationByCid(@PathVariable(value = "cid") String cid){
        aiConversationService.delConversationByCid(cid);
        return BaseResult.ok();
    }

    @GetMapping("/conversation/history")
    public BaseResult<List<ConversationHistoryVO>> getAiConversationHistoryList(@RequestParam("cid") String cid) {
        List<AiConversationHistory> histories = aiConversationHistoryService.queryAiConversationHistoryListDesc(cid);
        if (histories.isEmpty()) {
            return BaseResult.ok(Collections.emptyList());
        }
        List<ConversationHistoryVO> voList = new ArrayList<>();
        for (AiConversationHistory history : histories) {
            voList.add(new ConversationHistoryVO(
                    history.getId() + "-" + RandomStringUtils.random(6), history.getUserContent(),
                    true,
                    DateTimeUtil.formatConversationHistoryTime(history.getCreateTime())));
            voList.add(new ConversationHistoryVO(
                    history.getId() + "-" + RandomStringUtils.random(6), history.getAiContent(),
                    false,
                    DateTimeUtil.formatConversationHistoryTime(history.getCreateTime())));
        }
        return BaseResult.ok(voList);
    }
}
