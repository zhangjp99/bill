/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.cloud;

import com.eova.common.base.BaseController;

/**
 * Eova云
 *
 * @author Jieven
 * @date 2015-1-6
 */
public class CloudController extends BaseController {

    public void index() {
        render("/eova/cloud/index.html");
    }

}