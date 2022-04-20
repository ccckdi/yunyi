package com.cy.yunyi.portal.controller;

import com.cy.yunyi.portal.annotation.LoginUser;
import com.cy.yunyi.portal.controller.validator.Order;
import com.cy.yunyi.portal.controller.validator.Sort;
import com.cy.yunyi.portal.service.CollectService;
import com.cy.yunyi.common.api.CommonResult;
import com.cy.yunyi.model.UmsCollect;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author caihx
 * @Description: 收藏夹Controller
 * @Date 2022/2/14
 */
@RestController
@Api(tags = "CollectController", description = "收藏夹Controller")
@RequestMapping("/collect")
public class CollectController {

    @Autowired
    private CollectService collectService;

    /**
     * 用户收藏列表
     *
     * @param userId 用户ID
     * @param type   类型，如果是0则是商品收藏，如果是1则是专题收藏
     * @param pageNum   分页页数
     * @param pageSize   分页大小
     * @return 用户收藏列表
     */
    @ApiOperation("获取收藏夹列表")
    @GetMapping("/list")
    public CommonResult list(@LoginUser Long userId,
                             @NotNull Integer type,
                             @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                             @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                             @Sort @RequestParam(defaultValue = "create_time") String sort,
                             @Order @RequestParam(defaultValue = "desc") String order) {
        if (userId == null) {
            return CommonResult.validateFailed();
        }
        List<UmsCollect> collectList = collectService.queryByType(userId, type, pageNum, pageSize, sort, order);
        return CommonResult.success(collectList);
    }

    /**
     * 用户收藏添加或删除
     * @param userId 用户ID
     * @param type 类型，如果是0则是商品收藏，如果是1则是专题收藏
     * @param valueId 商品id或专题id
     * @return
     */
    @ApiOperation("用户收藏添加或删除")
    @PostMapping("/addordelete")
    public CommonResult addordelete(@LoginUser Long userId,
                                    @RequestParam(value = "type") Integer type,
                                    @RequestParam(value = "valueId") Long valueId) {
        if (userId == null) {
            return CommonResult.validateFailed();
        }

        UmsCollect umsCollect = collectService.queryByTypeAndValue(userId, type, valueId);

        Integer result = 0;
        if (umsCollect == null){
            UmsCollect insertUmsCollect = new UmsCollect();
            insertUmsCollect.setUserId(userId);
            insertUmsCollect.setValueId(valueId);
            insertUmsCollect.setType(type);
            result = collectService.addCollect(insertUmsCollect);
        }else {
            result = collectService.deleteById(umsCollect.getId());
        }

        if (result > 0){
            return CommonResult.success();
        }

        return CommonResult.failed();
    }
}
