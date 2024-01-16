package com.yifan.admin.api.model.vo;

import com.yifan.admin.api.entity.Music;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: TODO
 * @author: wyf
 * @date: 2023/5/8
 */
@Data
@ApiModel(value = "音乐VO")
public class MusicVO {

    @ApiModelProperty(value = "音频名称")
    private String name;

    @ApiModelProperty(value = "作者")
    private String artist;

    @ApiModelProperty(value = "音频url")
    private String url;

    @ApiModelProperty(value = "封面")
    private String cover;



    public MusicVO(){

    }

    public MusicVO(Music player){
        if (player != null){
            this.name = player.getTitle();
            this.artist = player.getArtist();
            this.url = player.getSrc();
            this.cover = player.getPic();
        }
    }

}
