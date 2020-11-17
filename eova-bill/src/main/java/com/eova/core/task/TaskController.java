/**
 * Copyright (c) 2015-2020 EOVA.CN. All rights reserved.
 * 
 * Licensed to: 上海蜜炬信息科技有限公司(1613965675@qq.com)
 * Licensed under the EPPL license: http://eova.cn/eppl.txt
 * Software copyright registration number:2020SR0109251
 * For authorization, please contact: admin@eova.cn
 */
package com.eova.core.task;

import org.quartz.JobKey;

import com.eova.common.Easy;
import com.eova.common.base.BaseController;
import com.eova.model.Task;
import com.eova.plugin.quartz.QuartzPlugin;

/**
 * 定时任务
 * 
 * @author Jieven
 * 
 */
public class TaskController extends BaseController {

	// 启动任务
	public void start() {
		int id = getParaToInt(0);

		Task task = Task.dao.findById(id);

		try {
			String className = task.getStr("clazz");

			// 恢复任务
			JobKey jobKey = JobKey.jobKey(className, className);
			QuartzPlugin.scheduler.resumeJob(jobKey);

			Task.dao.updateState(id, Task.STATE_START);
		} catch (Exception e) {
			e.printStackTrace();
			renderJson(new Easy("任务启动失败！"));
		}

		renderJson(new Easy());
	}

	// 暂停任务
	public void stop() {
		int id = getParaToInt(0);

		Task task = Task.dao.findById(id);

		try {
			String className = task.getStr("clazz");

			// 暂停任务
			JobKey jobKey = JobKey.jobKey(className, className);
			QuartzPlugin.scheduler.pauseJob(jobKey);

			Task.dao.updateState(id, Task.STATE_STOP);
		} catch (Exception e) {
			e.printStackTrace();
			renderJson(new Easy("任务停止失败！"));
		}

		renderJson(new Easy());
	}

	// 立即运行一次任务
	public void trigger() {
		int id = getParaToInt(0);

		Task task = Task.dao.findById(id);

		try {
			String className = task.getStr("clazz");

			// 立即触发一次
			JobKey jobKey = JobKey.jobKey(className, className);
			QuartzPlugin.scheduler.triggerJob(jobKey);

		} catch (Exception e) {
			e.printStackTrace();
			renderJson(new Easy("任务停止失败！"));
		}

		renderJson(new Easy());
	}

}