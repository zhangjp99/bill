package com.oss.Manager;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import com.oss.beans.APIRequests;
import com.oss.beans.Devs;

/*主要功能：
* 1. 存储所有的api请求的数据
* 2. 根据预先配置的参数进行输出或处理*/
public class DebugManager {
    private static List<APIRequests> responses=new ArrayList<>(100);

    public static void getAll(Devs devs)
    {
        try {
            for (APIRequests apiRequests : responses) {
                StringBuilder stringBuilder = new StringBuilder();
                if (devs.isIsoutput()) {
                    stringBuilder.append(apiRequests.getApiname()).append("调用成功。").append("耗时：[").append(apiRequests.getTime()).append("ms]。");
                    if (devs.isIsdebug()) {
                        stringBuilder.append("传参：[").append(URLDecoder.decode(apiRequests.getRequestparms(), "utf-8")).append("]。样例:[").append(apiRequests.getRequesturls())
                                .append("],返回结果:[").append(apiRequests.getResponse()).append("]");
                    }
                    System.out.println("----"+stringBuilder.toString());
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void addResponse(APIRequests a)
    {
        responses.add(a);
    }

    public static void addResponse(long time,String apiname,String requestparms,String requesturls,String response)
    {
        APIRequests apiRequests=new APIRequests();
        apiRequests.setTime(time);
        apiRequests.setApiname(apiname);
        apiRequests.setRequestparms(requestparms);
        apiRequests.setRequesturls(requesturls);
        apiRequests.setResponse(response);
        responses.add(apiRequests);
    }
}
