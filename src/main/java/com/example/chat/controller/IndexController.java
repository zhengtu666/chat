package com.example.chat.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: liuhuan
 * @Description: 首页跳转
 * @date: 2019/12/1
 */
@Controller
@Slf4j
public class IndexController extends BaseController{

    @GetMapping("/test")
    @ResponseBody
    public String test(){
        return "ok";
    }

    @GetMapping("/chat")
    public String chat(){
        return "chat";
    }

}
