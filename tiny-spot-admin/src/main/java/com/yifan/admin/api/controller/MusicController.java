package com.yifan.admin.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yifan.admin.api.service.MusicService;
import com.yifan.admin.api.annotition.Log;
import com.yifan.admin.api.annotition.Right;
import com.yifan.admin.api.model.params.MusicListParam;
import com.yifan.admin.api.model.params.SaveOrUpdateMusicParam;
import com.yifan.admin.api.entity.Music;
import com.yifan.admin.api.enums.BusinessTypeEnum;
import com.yifan.admin.api.model.vo.MusicVO;
import com.yifan.admin.api.result.BaseResult;
import com.yifan.admin.api.result.CommonPage;
import com.yifan.admin.api.utils.DateTimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: TODO
 * @author: wyf
 * @date: 2023/5/12
 */
@RestController
@Api(tags = "系统工具-音乐管理")
@RequestMapping("/music")
public class MusicController {

    @Resource
    private MusicService musicService;

    @ApiOperation("音乐列表")
    @Right(rightsOr = "sys:music:list")
    @RequestMapping(value = "/pageInfo", method = RequestMethod.GET)
    public BaseResult<CommonPage<Music>> getMusicPageList(MusicListParam param) {
        Page<Music> page = new Page<>(param.getPageNum(), param.getPageSize());
        LambdaQueryWrapper<Music> queryWrapper = new QueryWrapper<Music>().lambda();
        if (param.getSortOrder().contains("desc")){
            queryWrapper.orderByDesc(Music::getSort);
        }else {
            queryWrapper.orderByAsc(Music::getSort);
        }

        if (StringUtils.isNotBlank(param.getKeyword())) {
            queryWrapper
                    .like(Music::getTitle, param.getKeyword())
                    .or()
                    .like(Music::getArtist, param.getKeyword())
                    .or()
                    .like(Music::getAlbum, param.getKeyword());
        }
        Page<Music> page1 = musicService.page(page, queryWrapper);
        return BaseResult.ok(CommonPage.restPage(page1));
    }

    @ApiOperation("新增音乐")
    @Log(title = "音乐管理", businessType = BusinessTypeEnum.INSERT)
    @Right(rightsOr = "sys:music:add")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public BaseResult<Boolean> saveMusic(@RequestBody SaveOrUpdateMusicParam param) {
        Music musicPlayer = new Music();
        BeanUtils.copyProperties(param, musicPlayer);
        musicPlayer.setCreateTime(DateTimeUtil.currentDate());
        return BaseResult.ok(musicService.save(musicPlayer));
    }

    @ApiOperation("修改音乐")
    @Log(title = "音乐管理", businessType = BusinessTypeEnum.UPDATE)
    @Right(rightsOr = "sys:music:update")
    @RequestMapping(value = "/update/{musicId}", method = RequestMethod.POST)
    public BaseResult updateMusic(@PathVariable(value = "musicId") Long musicId,
                                  @RequestBody SaveOrUpdateMusicParam param) {
        Music musicPlayer = new Music();
        musicPlayer.setId(musicId);
        BeanUtils.copyProperties(param, musicPlayer);
        return BaseResult.ok(musicService.updateById(musicPlayer));
    }

    @ApiOperation("删除音乐")
    @Log(title = "音乐管理", businessType = BusinessTypeEnum.DELETE)
    @Right(rightsOr = "sys:music:delete")
    @RequestMapping(value = "/remove/{musicId}", method = RequestMethod.DELETE)
    public BaseResult<Boolean> deleteMusic(@PathVariable(value = "musicId") Long musicId) {
        boolean removeById = musicService.removeById(musicId);
        return BaseResult.ok(removeById);
    }

    @ApiOperation("获取音乐根据id")
    @Right(rightsOr = "sys:music:list")
    @RequestMapping(value = "/getById/{musicId}", method = RequestMethod.GET)
    public BaseResult<Music> getById(@PathVariable(value = "musicId") Long musicId) {
        Music playerServiceById = musicService.getById(musicId);
        return BaseResult.ok(playerServiceById);
    }

    @ApiOperation("音乐播放器-分页列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public BaseResult<List<MusicVO>> pageList(){
        LambdaQueryWrapper<Music> wrapper = new QueryWrapper<Music>()
                .lambda()
                .orderByDesc(Music::getSort);
        List<Music> list = musicService.list(wrapper);
        List<MusicVO> result = list.stream().map(MusicVO::new).collect(Collectors.toList());
        return BaseResult.ok(result);
    }
}
