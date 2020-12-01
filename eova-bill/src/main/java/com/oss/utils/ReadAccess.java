package com.oss.utils;




import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import com.jfinal.kit.PathKit;
import com.oss.model.Qx;
import com.oss.model.QxList;
import com.oss.model.Tss;

/**
 * Created by tangdonghai on 07/09/2017.
 */

public class ReadAccess {

    private static final String jsonFileName = "gcodeList.json";


    public static Set<String> readFile(List<String> gcodes) {
    	 Set<String> outputs = new HashSet<String>();

        try {
        	 String path = PathKit.getRootClassPath() + File.separator+jsonFileName;
        	 File jsonFile = new File(path);
            InputStreamReader isr = new InputStreamReader(new FileInputStream(jsonFile), "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            br.close();
            isr.close();

            Gson gson = new Gson();
            QxList qxList = gson.fromJson(builder.toString(), QxList.class);


            for (String code : gcodes) {//输入的馆代码
                for (Qx qx : qxList.getQx()) {
                    String ename = "";
                    String locEname = "";
                    for (Tss tss : qx.getTss()) {
                        if (!tss.getLevel().equals("基层服务点")) {//是区或是市馆？
                            ename = tss.getEname();
                        }
                        if (code.equalsIgnoreCase(tss.getCode())) {
                            locEname = tss.getEname();
                        }
                        if (!ename.isEmpty() && !locEname.isEmpty()) {
                            outputs.add(ename);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return  outputs;

    }
    public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
//		list.add("G17");
//		list.add("J150-F1");
		list.add("J123");
		Set<String> set =readFile(list);
		for (String str : set) {  
		      System.out.println(str);  
		} 
	}


}
