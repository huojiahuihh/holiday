package com.baidu.shop.dto;

import com.baidu.shop.base.BaseDTO;
import com.baidu.shop.validate.group.MingruiOperation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@ApiModel(value = "spu数据传输DTO")
@Data
public class SpuDTO extends BaseDTO {

    @ApiModelProperty(value = "主键" ,example = "1")
    @NotNull(message = "主键不能为空",groups = {MingruiOperation.Update.class})
    private Integer id;

    @ApiModelProperty(value = "标题")
    @NotEmpty(message = "标题不能为空",groups = {MingruiOperation.Add.class,MingruiOperation.Update.class})
    private String title;

    @ApiModelProperty(value = "子标题")
    private String subTitle;

    @ApiModelProperty(value = "一级类目id",example = "1")
    @NotNull(message = "一级类目id不能为空",groups = {MingruiOperation.Add.class ,MingruiOperation.Update.class})
    private Integer cid1;

    @ApiModelProperty(value = "二级类目id",example = "1")
    @NotNull(message = "二级类目的不能为空",groups = {MingruiOperation.Add.class ,MingruiOperation.Update.class})
    private Integer cid2;

    @ApiModelProperty(value = "三级类目id",example = "1")
    @NotNull(message = "三级类目的不能为空",groups = {MingruiOperation.Add.class ,MingruiOperation.Update.class})
    private Integer cid3;

    @ApiModelProperty(value = "商品所属品牌id", example = "1")
    @NotNull(message = "商品所属品牌id不能为空",groups = {MingruiOperation.Add.class ,MingruiOperation.Update.class})
    private Integer brandId;

    //不需要验证,新增时直接设置默认值
    @ApiModelProperty(value = "是否上架，0下架，1上架", example = "1")
    @NotNull(message = "是否上架不能为空",groups = {MingruiOperation.Add.class ,MingruiOperation.Update.class})
    private Integer saleable;

    //不需要验证,新增时直接设置默认值
    @ApiModelProperty(value = "是否有效，0已删除，1有效", example = "1")
    @NotNull(message = "是否有效不能为空",groups = {MingruiOperation.Add.class ,MingruiOperation.Update.class})
    private Integer valid;

    //不需要验证,新增时直接设置默认值
    @ApiModelProperty(value = "添加时间")
    @NotNull(message = "添加时间不能为空",groups = {MingruiOperation.Add.class,MingruiOperation.Update.class})
    private Date createTime;

    //不需要验证,新增时直接设置默认值,修改时使用java代码赋值
    @ApiModelProperty(value = "最后修改时间")
    private Date lastUpdateTime;

    private String brandName;

    private String cateGoryName;

    @ApiModelProperty(value = "大字段数据")
    private SpuDetailDTO spuDetail;

    @ApiModelProperty(value = "sku属性数据集合")
    private List<SkuDTO> skus;
}
