package com.example.community.controller;

import com.example.community.annotation.LoginRequired;
import com.example.community.entity.User;
import com.example.community.service.UserService;
import com.example.community.util.CommunityUtil;
import com.example.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private HostHolder hostHolder;

    @LoginRequired  //登录之后才能上传头像
    @RequestMapping(path = "/setting",method = RequestMethod.GET)
    public String getSettingPage(){
        return "/site/setting";
    }

    @LoginRequired  //需要登录之后，才能上传
    //更新用户头像
    @RequestMapping(path = "/upload" ,method = RequestMethod.POST)
    public String UploadHeader(MultipartFile headerImage, Model model)
    {
        if(headerImage==null){
            model.addAttribute("error","您还没有选择图片!");
            return "/site/setting";
        }
        String filename = headerImage.getOriginalFilename();
        String suffix = filename.substring(filename.lastIndexOf("."));
        if(StringUtils.isBlank(suffix)){    //判断非空，但此时并不能保证上传的文件为.jpg、.png 也可能为.exe
            model.addAttribute("error","格式不对头!");
            return "/site/setting";
        }
        //生成新的随机文件名
        filename= CommunityUtil.generateUUID()+suffix;
        //文件存放路径
        File file=new File(uploadPath+"/"+filename);
        try {
            //存储文件
            headerImage.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //上述步骤，仅仅是保存了客户端上传的图片 ，此时还没有更新用户的头像
        //更新路径 http://localhost:8080/community/user/header/xxx.png
        User user = hostHolder.getUser();
        String headerUrl=domain+contextPath+"/user/header/"+filename;
        userService.updateHeader(user.getId(),headerUrl);
        return "redirect:/index";
    }

    //获取用户头像
    @RequestMapping(path = "/header/{filename}",method = RequestMethod.GET)
    public void getHeader(@PathVariable("filename")String filename, HttpServletResponse response){
        //图片服务器 存放路径 D:/work/
        filename=uploadPath+"/"+filename;
        //图片后缀
        String suffix=filename.substring(filename.lastIndexOf("."));
        //响应图片
        response.setContentType("image/"+suffix);

        try (
                OutputStream os= response.getOutputStream();
                //字节输入流 读图片服务器的图片
                FileInputStream inputStream = new FileInputStream(filename);
                ){
            byte[] buffer=new byte[1024];
            int b=0;
            while((b=inputStream.read(buffer))!=-1){
                os.write(buffer,0,b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
