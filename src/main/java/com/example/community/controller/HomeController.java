package com.example.community.controller;

import com.example.community.entity.DiscussPost;
import com.example.community.entity.Page;
import com.example.community.entity.User;
import com.example.community.service.DiscussPostService;
import com.example.community.service.FollowService;
import com.example.community.service.LikeService;
import com.example.community.service.UserService;
import com.example.community.util.CommunityConstant;
import com.example.community.util.HostHolder;
import org.apache.catalina.Host;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController implements CommunityConstant {
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private UserService userService;
    @Autowired
    private LikeService likeService;
    @Autowired
    private FollowService followService;
    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path="/",method = RequestMethod.GET)
//    @ResponseBody
    public String root(){
        return "forward:/index";
    }

    @RequestMapping(path = "/index",method = RequestMethod.GET)
    public String getIndex(Model model, Page page,
                           @RequestParam(name = "orderMode",defaultValue = "0") int orderMode){
        //方法调用前，SpringMVC会自动实例化Model和Page,并将Page注入Model
        page.setRows(discussPostService.findDiscussPostsRows(0));
        page.setPath("/index?orderMode="+orderMode);

        List<DiscussPost>  list = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit(),orderMode);
        List<Map<String,Object>> discussPosts=new ArrayList<>();    //新建数组 存储list和user
        if(list!=null) {    //判断非空
            for (DiscussPost post : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", post);
                User user = userService.findUserById(post.getUserId());
                map.put("user", user);
                long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId());
                map.put("likeCount",likeCount);
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts",discussPosts);
        model.addAttribute("orderMode",orderMode);
        return "/index";
    }

   @GetMapping(path = "/error")
    public String getError(){
        return "/error/500";
   }

    //个人主页
    @GetMapping("/user/profile/{userId}")
    public String getProfilePage(@PathVariable("userId")String userId,Model model){
        User user=userService.findUserById(Integer.parseInt(userId));
        if(user==null){
            throw new RuntimeException("该用户不存在!");
        }
        //用户
        model.addAttribute("user",user);
        //用户获得点赞的数量
        int likeCount = likeService.findUserLikeCount(Integer.parseInt(userId));
        model.addAttribute("likeCount",likeCount);

        //关注数量
        long followeeCount = followService.findFolloweeCount(Integer.parseInt(userId),ENTITY_TYPE_USER);
        model.addAttribute("followeeCount",followeeCount);
        //粉丝数量
        long followerCount = followService.findFollowerCount(ENTITY_TYPE_USER,Integer.parseInt(userId));
        model.addAttribute("followerCount",followerCount);
        //是否已经关注
        boolean hasFollowed=false;
        if(hostHolder.getUser()!=null) {
            hasFollowed = followService.hasFollowed(hostHolder.getUser().getId(),ENTITY_TYPE_USER,Integer.parseInt(userId));
        }
        model.addAttribute("hasFollowed",hasFollowed);
        return "/site/profile";
    }

}
