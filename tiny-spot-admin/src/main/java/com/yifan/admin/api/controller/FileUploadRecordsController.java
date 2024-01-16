package com.yifan.admin.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yifan.admin.api.service.FileUploadRecordService;
import com.yifan.admin.api.annotition.Right;
import com.yifan.admin.api.entity.FileUploadRecord;
import com.yifan.admin.api.model.params.FileUploadRecordsParam;
import com.yifan.admin.api.result.BaseResult;
import com.yifan.admin.api.result.CommonPage;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author TaiYi
 * @ClassName
 * @date 2023/7/14 12:19
 */
@Slf4j
@RestController
@RequestMapping("/up-record")
public class FileUploadRecordsController {

    @Autowired
    private FileUploadRecordService fileUploadRecordService;

    @ApiOperation("分页获取上传记录")
    @Right(rightsOr = "sys:upRecord:list")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResult<CommonPage<FileUploadRecord>> list(FileUploadRecordsParam param) {

        Page<FileUploadRecord> page = new Page<>(param.getPageNum(), param.getPageSize());

        LambdaQueryWrapper<FileUploadRecord> queryWrapper = new QueryWrapper<FileUploadRecord>().lambda();
        if (param.getSortOrder().contains("desc")){
            queryWrapper.orderByDesc(FileUploadRecord::getCreateTime);
        }else {
            queryWrapper.orderByAsc(FileUploadRecord::getCreateTime);
        }

        if (StringUtils.isNotBlank(param.getBeginTime()) && StringUtils.isNotBlank(param.getEndTime())){
            queryWrapper.between(FileUploadRecord::getCreateTime, param.getBeginTime(), param.getEndTime());
        }

        if (StringUtils.isNotBlank(param.getKeyword())) {
            queryWrapper.like(FileUploadRecord::getFileName, param.getKeyword())
                    .or()
                    .like(FileUploadRecord::getFileUrl, param.getKeyword())
                    .or()
                    .like(FileUploadRecord::getExcerptHash, param.getKeyword());
        }

        Page<FileUploadRecord> recordPage = fileUploadRecordService.page(page, queryWrapper);

        return BaseResult.ok(CommonPage.restPage(recordPage));
    }

    @ApiOperation("删除文件记录")
    @Right(rightsOr = "sys:upRecord:delete")
    @RequestMapping(value = "/remove/{id}", method = RequestMethod.DELETE)
    public BaseResult<Boolean> removeById(@PathVariable(value = "id") Long fileRecordId){
        log.info("remove file record, id: {}", fileRecordId);
        boolean removeById = fileUploadRecordService.removeById(fileRecordId);
        return BaseResult.ok(removeById);
    }
}
