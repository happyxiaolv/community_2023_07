package com.example.community.util;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {
    //替换符
    private static final String REPLACEMENT="***";

    private TreeNode root=new TreeNode();

    //初始化敏感树 在构造器之后执行，类初始化时就被执行
    @PostConstruct
    public void init(){
       try(
               InputStream is=this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
               BufferedReader reader=new BufferedReader(new InputStreamReader(is));
               )
       {
           String keyword;
           while((keyword=reader.readLine())!=null){
               this.addKeyword(keyword);
           }
       }catch (IOException e){
           System.out.println(e.getMessage());
       }
    }

    //将一个敏感词添加到前缀树中
    public void addKeyword(String keyword){
        //就是按某种规则构造一棵树
        TreeNode tmpNode=root;
        for(int i=0;i<keyword.length();i++){
            char c = keyword.charAt(i);
            TreeNode subnode = tmpNode.getSubNodes(c);
            //没有该子结点
            if(subnode==null){
                subnode=new TreeNode();
                tmpNode.addSubNodes(c,subnode);
            }
            //进入下次循环
            tmpNode=subnode;
            //标志叶结点
            if(i==keyword.length()-1){
                tmpNode.setKeywordsEnds(true);
            }
        }
    }
    //检测过滤  （真正外界调用的）
    public String filter(String text){
        if(StringUtils.isBlank(text)){
            return null;
        }
        //三个指针 指针1
        TreeNode tmpNode=root;
        //指针2
        int begin=0;
        //指针3  比指针2提前到结尾
        int position=0;
        //存放结果
        StringBuilder sb=new StringBuilder();

        //指针3先开始
        while(position<text.length()){
            char c=text.charAt(position);
            //跳过符号
            if(isSymbol(c)){
                //若指针1处于根节点，保存此符号，指针2向下走一步
                if(tmpNode==root){
                    sb.append(c);
                    begin++;
                }
                //无论符号在中间或开头 ※赌※博※，指针3都向下走一步
                position++;
                continue;
            }
            //检查下级结点
            tmpNode=tmpNode.getSubNodes(c);
            if(tmpNode==null){
                //以begin为开头的字符串不是敏感词
                sb.append(text.charAt(begin));
                //进入下一个位置
                position=++begin;
                //重新指向根节点
                tmpNode=root;
            }else if(tmpNode.isKeywordsEnds()){
                //发现敏感词 将begin~position位置 替换
                sb.append(REPLACEMENT);
                //进入下一个位置
                begin=++position;
                //重新指向根节点
                tmpNode=root;
            }else{
                //检查下一个字符
                ++position;
            }
        }
        //position到字符串结尾，但是字符串结尾不是敏感词的字符，并未记录
        //将最后一批字符 计入结果
        sb.append(text.substring(begin));
        return sb.toString();
    }
    //判断是否为符号
    public boolean isSymbol(Character c){
        //0x2E80~0x9FFF 东亚文字
        return !CharUtils.isAsciiAlphanumeric(c)&&(c<0x2E80||c>0x9FFF);
    }
    //前缀树数据结构
    private class TreeNode{
        private boolean isKeywordsEnds=false;
        private Map<Character,TreeNode> subnodes=new HashMap<>();
        public boolean isKeywordsEnds() {
            return isKeywordsEnds;
        }
        public void setKeywordsEnds(boolean keywordsEnds) {
            isKeywordsEnds = keywordsEnds;
        }
        //添加子节点
        public void addSubNodes(Character c,TreeNode node){
            subnodes.put(c,node);
        }
        //获取子节点
        public TreeNode getSubNodes(Character c){
            return subnodes.get(c);
        }
    }

}
