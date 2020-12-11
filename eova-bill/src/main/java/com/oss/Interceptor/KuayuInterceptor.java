package com.oss.Interceptor;

import javax.servlet.http.HttpServletResponse;

import com.eova.common.base.BaseController;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

import jodd.net.HttpMethod;

public class KuayuInterceptor  implements Interceptor  {

	@Override
	public void intercept(Invocation inv) {
			if(inv.isActionInvocation()) {
				BaseController ctrl = (BaseController) inv.getController();
				//HttpServletResponse response = ctrl.getResponse();
//				response.setHeader("Access-Control-Allow-Origin", "*");
				ctrl.getResponse().setHeader("Access-Control-Allow-Origin", "*");
				ctrl.getResponse().setHeader("Access-Control-Allow-Methods", "*");
				ctrl.getResponse().setHeader("Access-Control-Allow-Credentials", "true");
				ctrl.getResponse().setHeader("Access-Control-Allow-Headers", "DNT,X-Mx-ReqToken,Keep-Alive,User-Agent,X-Requested-With,X_Requested_With,If-Modified-Since,Cache-Control,Content-Type, Accept-Language, Origin, Accept-Encoding");
				System.out.println(ctrl.getRequest().getMethod());
				System.out.println(HttpMethod.OPTIONS.name());
				if(ctrl.getRequest().getMethod().equalsIgnoreCase(HttpMethod.OPTIONS.name())) {
					ctrl.getResponse().setStatus(HttpServletResponse.SC_OK);
					//inv.invoke();
					System.out.println(HttpServletResponse.SC_OK);
					return;
				}
			}
			inv.invoke();
	}

}
