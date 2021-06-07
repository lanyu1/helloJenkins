package com.lanyu.jenkins.hellojenkins.common.utils;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lanyu
 * @date 2021年05月27日 15:30
 */
@Slf4j
public class ResponseUtil {

    /**
     * 使用response输出JSON
     * @param response
     * @param resultMap
     */
    public static void out(HttpServletResponse response, Map<String,Object> resultMap){
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            response.getOutputStream().write(new Gson().toJson(resultMap).getBytes());
        }catch (Exception e){
            log.error(e + ", 输出json出错");
        }
    }

    /**
     * 输出不含数据的json数据
     * @param flag
     * @param code
     * @param msg
     * @return
     */
    public static Map<String, Object> resultMap(boolean flag, Integer code, String msg) {

        return resultMap(flag, code, msg, null);
    }

    /**
     * 输出含有object对象数据的json
     * @param flag 标识
     * @param code code码
     * @param msg 说明
     * @param data 数据
     * @return
     */
    public static Map<String, Object> resultMap(boolean flag, Integer code, String msg, Object data) {

        Map<String, Object> resultMap = new HashMap<String, Object>(16);
        resultMap.put("success", flag);
        resultMap.put("message", msg);
        resultMap.put("code", code);
        resultMap.put("timestamp", System.currentTimeMillis());
        if (data != null) {
            resultMap.put("result", data);
        }
        return resultMap;
    }
}
