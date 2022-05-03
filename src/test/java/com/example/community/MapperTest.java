package com.example.community;

import com.example.community.dao.DiscussPostMapper;
import com.example.community.dao.LoginTicketMapper;
import com.example.community.dao.MessageMapper;
import com.example.community.dao.UserMapper;
import com.example.community.entity.DiscussPost;
import com.example.community.entity.LoginTicket;
import com.example.community.entity.Message;
import com.example.community.entity.User;
//import org.junit.jupiter.api.Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.*;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTest {

    @Autowired
    private UserMapper userMapper;
    @Test
    public void testSelectUser(){
        User user = userMapper.selectById(101);
        System.out.println(user);

        user = userMapper.selectByName("liubei");
        System.out.println(user);


        user= userMapper.selectByEmail("nowcoder101@sina.com");
        System.out.println(user);
    }
    @Test
    public void testInsertUser(){
        User user=new User();
        user.setUsername("test");
        user.setPassword("123456");
        user.setSalt("abc");
        user.setEmail("test@qq.com");
        user.setHeaderUrl("http://www.nowcoder.com/101.png");
        user.setCreateTime(new Date());

        int row = userMapper.insertUser(user);
        System.out.println(row);
        System.out.println(user.getId());
    }
    @Test
    public void testUpdate(){
        int rows = userMapper.updateStatus(150, 1);
        System.out.println(rows);
        int r = userMapper.updateHeader(150, "http://www.nowcoder.com/102.png");
        System.out.println(r);
        int hello =userMapper.updatePassword(150,"hello");
        System.out.println(hello);
    }

    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Test
    public void testSelectPosts(){
        List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(0, 0, 10,0);
        for (DiscussPost list : discussPosts) {
            System.out.println(list);
        }
        int rows = discussPostMapper.selectDiscussRows(0);
        System.out.println(rows);
    }


    @Autowired
    private LoginTicketMapper loginTicketMapper;
    @Test
    public void testInsertLoginTicket(){
        LoginTicket loginTicket=new LoginTicket();

        loginTicket.setId(101);
        loginTicket.setTicket("abc");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis()+1000*10*60));

        loginTicketMapper.insertLoginTicket(loginTicket);
    }
    @Test
    public void testSelectLoginTicket(){
        LoginTicket loginTicket = loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket);
        loginTicketMapper.updateStatus("abc",1);
        loginTicket = loginTicketMapper.selectByTicket("abc");
        System.out.println(loginTicket);
    }


    @Autowired
    private MessageMapper messageMapper;
    @Test
    public void testMessage(){
        List<Message> messages=messageMapper.selectConversations(111,0,20);
//        for(Message m:messages){
//            System.out.println(m);
//        }

//        int count = messageMapper.selectConversationCount(111);
//        System.out.println(count);

        List<Message> list = messageMapper.selectLetters("111_112", 0, 10);
//        for (Message message : list) {
//            System.out.println(message);
//        }

//        int count = messageMapper.selectLetterCount("111_112");
//        System.out.println(count);

        int count = messageMapper.selectLetterUnreadCount(131, "111_131");
        System.out.println(count);
    }

    @Test
    public void test(){
        String s="abccccdd";
        char[] arr = s.toCharArray();
        Map<Character, Object> map = new HashMap<>();

        int count=0;
        for(int i=0;i<arr.length;i++){
            for(int j=0;j<arr.length;j++){
                if(arr[i]==arr[j]){
                    count++;
                }
            }
            map.put(arr[i],count);
            count=0;
        }

        Set<Map.Entry<Character, Object>> entries = map.entrySet();
        int avg=0;
        for(Map.Entry<Character,Object> entry:entries){
            System.out.println(entry.getKey()+","+entry.getValue());
            avg+=(Integer)entry.getValue()/2*2;
            if((Integer) entry.getValue()%2==1&&avg%2==0){
                avg++;
            }
        }
        System.out.println(avg);
    }

    class Student implements Comparable<Student>{
        private String name;
        private int age;

        public Student() {
        }

        public Student(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public int compareTo(Student o) {
            //年龄从小到大
            int num=this.age-o.age;
            //年龄相同，按照姓名排序
            int num2= num==0?this.name.compareTo(o.name):num;
            return num2;
        }
    }

    @Test
    public void comparatorTest() {
        PriorityQueue<int[]> queue = new PriorityQueue<int[]>(new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return o2[1] - o1[1];
            }
        });
        queue.offer(new int[]{1, 3});
        queue.offer(new int[]{5, 2});
        System.out.println(queue.peek()[0]);

        TreeSet<Integer> ts = new TreeSet<Integer>();
//        //添加元素
//        ts.add(10);
//        ts.add(40);
//        ts.add(30);
//        ts.add(50);
//        ts.add(20);
//        for (Integer i : ts) {
//            System.out.println(i);
//            }

        TreeSet<Student> students=new TreeSet<>();
        students.add(new Student("A",29));
        students.add(new Student("B",28));
        students.add(new Student("C",39));
        students.add(new Student("C",39));
        for (Student student : students) {
            System.out.println(student.getName()+","+student.getAge());
        }
    }

    @Test
    public void test4(){
        int[] nums={1,3,4,2,2};
        int n=nums.length;
        int l=1,r=n-1,ans=-1;
        //二分  cnt[i] 表示nums中<=i的个数
        while(l<=r){
            int cnt=0;
            int mid=(l+r)>>1;
            for(int i=0;i<n;i++){
                if(nums[i]<=mid){
                    cnt++;
                    System.out.println("mid="+mid+",nums[i]="+nums[i]+" cnt="+cnt);
                }
            }
            if(cnt<=mid) l=mid+1;
            else{
                r=mid-1;
                ans=mid;
            }
        }
        System.out.println(ans);
    }

    @Test
    public void test3(){
        int n=3;
        System.out.println(1<<n);   //相当于2的n次幂
        for(int mask=0;mask<(1<<n);++mask) {
            for (int i = 0; i < n; i++) {   //i=0 1 2
                if ((mask&(1<<i))!=0) {
                    System.out.print("mask=" + mask + ",1<<i=" + (1 << i));
                    System.out.println();
                }
            }
        }
    }

    @Test
    public void test_method(){
        StringBuilder builder=new StringBuilder();
        builder.append('a');
        builder.append('b');
        System.out.println(builder.length());
        builder.deleteCharAt(builder.length()-1);
        System.out.println(builder.toString());
    }

    @Test
    public void test6070(){
        String s=digitSum("11111222223",3);
        System.out.println(s);
    }
    public String digitSum(String s,int k){
        String res="";
        while(s.length()>k){
            int count=(int)Math.ceil(s.length()/3);
            System.out.println(count);
//            String[] strs=s.split("[\\d]",count);
            for(int i=0;i<count;i++){
            }
            s=res;
        }
        return s;
    }
    public String countSum(String str){
        Integer sum=0;
        String[] s=str.split("[\\d]");
        for(int i=0;i<s.length;i++){
            sum+=Integer.valueOf(s[i]);
        }
        return sum.toString();
    }


}
