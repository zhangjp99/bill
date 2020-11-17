/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.handler;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;
import com.jfinal.kit.StrKit;

/**
 * Ban掉禁止访问的资源
 *
 *
 * @创建者：Jieven
 * @创建时间：2017-2-20 下午3:14:06
 */
public class UrlBanHandler extends Handler {

    private Pattern skipedUrlPattern;

    public UrlBanHandler(String skipedUrlRegx, boolean isCaseSensitive) {
        if (StrKit.isBlank(skipedUrlRegx))
            throw new IllegalArgumentException("The para excludedUrlRegx can not be blank.");
        skipedUrlPattern = isCaseSensitive ? Pattern.compile(skipedUrlRegx) : Pattern.compile(skipedUrlRegx, Pattern.CASE_INSENSITIVE);
    }

    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
        if (skipedUrlPattern.matcher(target).matches()) {
            try {
				System.err.println(skipedUrlPattern + " 文件禁止直接访问, 如果想直接访问静态网页,请改成 .htm 格式!");
                response.sendError(404);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        } else {
            next.handle(target, request, response, isHandled);
        }
    }
}