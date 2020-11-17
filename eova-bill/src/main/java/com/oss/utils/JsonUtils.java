package com.oss.utils;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.PathKit;
import com.oss.model.Qx;
import com.oss.model.QxList;


public class JsonUtils {
	
	 //读取json文件
    public static String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();;
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 读取文件转换map
     * @return
     */
    public static Map<String, Object> getLibraryMap() {
    	 String path = PathKit.getRootClassPath() + File.separator+"gcodeList.json";
         String json = readJsonFile(path);
         QxList request = JSONObject.parseObject(json.toString(), QxList.class);
         Map<String, Object> map = new  HashMap<String, Object>();
         List<Qx> qx = request.getQx();
         for (Qx qx2 : qx) {
        	 map.put(qx2.getCode(), qx2);
		}
         return map;
	}
    /**
     * 根据code 获取出name参数
     * @param code
     * @return
     */
    public static String findName(String code) {
    	Map<String, Object> map = getLibraryMap();
    	if(map!=null) {
    		if(map.get(code)!=null) {
    			Qx qx = (Qx) map.get(code);
    			return  qx.getName();
    		}
    	}
    	return null;
    }

    public static void main(String[] args) {
    	System.out.println(findName("SL"));
	}
}
