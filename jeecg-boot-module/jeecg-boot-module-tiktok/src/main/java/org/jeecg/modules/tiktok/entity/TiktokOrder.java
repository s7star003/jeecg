package org.jeecg.modules.tiktok.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("tiktok_order")
public class TiktokOrder implements Serializable {
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    private String orderId;
    private Date createTime;
    private Date updateTime;
    private String buyerUsername;
    private BigDecimal totalAmount;
    private String status;
}
