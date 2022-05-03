package com.example.community;

import org.apache.commons.lang3.builder.ToStringExclude;
import org.junit.Test;

import java.util.*;

public class test01 {

    @Test
    public void test1(){
        Map<Integer,Integer> map=new HashMap<>();
        int[] nums={3,2,1,5,6,4};
        int k=2;

        PriorityQueue<Integer> queue=new PriorityQueue<>(Collections.reverseOrder());
        for(int var:nums){
            queue.add(var);
        }
        System.out.println(queue.size());
        while(!queue.isEmpty()){
            System.out.println(queue.poll());
        }
    }

    @Test
    public void test2(){
        int[] nums={3,2,3};

        HashMap<Integer,Integer> map=new HashMap<>();
        Arrays.sort(nums);

        for(int var:nums){
            map.put(var,map.getOrDefault(var,0)+1);
        }

        for(Map.Entry<Integer,Integer> entry:map.entrySet()){
            System.out.println(entry.getKey()+"== "+entry.getValue());

            if(entry.getValue()>(nums.length/2))
                System.out.println("满足条件:"+entry.getKey());
        }
    }

    @Test
    public void test3()
    {
        List<Integer> list1=new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(4);
        System.out.println("list1:"+list1);
        List<Integer> list2=new ArrayList<>();
        list2.add(0);
        list2.add(1);
        list2.add(3);
        System.out.println("list2:"+list2);

        List<Integer> res=new ArrayList<>();
        int i=0,j=0;
//        for(;i<list1.size()&&j<list2.size();){
//            if(list1.get(i)<=list2.get(j)){
//                res.add(list1.get(i));
//                i++;
//            }else{
//                res.add(list2.get(j));
//                j++;
//            }
//        }
//        for(;i<list1.size();i++){
//            res.add(list1.get(i));
//        }
//        for(;j<list2.size();j++){
//            res.add(list2.get(j));
//        }
        while(i<list1.size()&&j<list2.size()){
            if(list1.get(i)<=list2.get(j)){
                res.add(list1.get(i++));
            }else res.add(list2.get(j++));
        }
        while(i<list1.size()) res.add(list1.get(i++));
        while(j<list2.size()) res.add(list2.get(j++));
        System.out.println(res);
    }
}
