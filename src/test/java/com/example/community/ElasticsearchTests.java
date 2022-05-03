package com.example.community;

import com.alibaba.fastjson.JSONObject;
//import com.example.community.config.ElasticsearchRestTemplateConfig;
import com.example.community.dao.DiscussPostMapper;
import com.example.community.dao.elasticsearch.DiscussPostRepository;
import com.example.community.entity.DiscussPost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
//import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.naming.directory.SearchResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class ElasticsearchTests {
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private DiscussPostRepository discussPostRepository;

    @Autowired
    private ElasticsearchTemplate elasticTemplate;

    @Test
    public void testInsert() {
        discussPostRepository.save(discussPostMapper.findDiscussPostById(241));
        discussPostRepository.save(discussPostMapper.findDiscussPostById(242));
        discussPostRepository.save(discussPostMapper.findDiscussPostById(243));
    }

    @Test
    public void testInertList() {
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(101, 0, 100,0));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(102, 0, 100,0));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(103, 0, 100,0));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(11, 0, 100,0));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(111, 0, 100,0));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(112, 0, 100,0));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(131, 0, 100,0));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(132, 0, 100,0));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(133, 0, 100,0));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(134, 0, 100,0));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(138, 0, 100,0));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(145, 0, 100,0));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(146, 0, 100,0));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(149, 0, 100,0));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(151, 0, 100,0));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(154, 0, 100,0));
    }

    @Test
    public void testUpdate() {
        DiscussPost post = discussPostMapper.findDiscussPostById(231);
        post.setContent("我诗经灌水");
        discussPostRepository.save(post);
    }

    @Test
    public void testDelete() {
        discussPostRepository.deleteAll();
    }

    @Test
    public void testSearchByTemplate() {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery("互联网寒冬", "title", "content"))
                .withSort(SortBuilders.fieldSort("type").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("score").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                .withPageable(PageRequest.of(0, 10))
                .withHighlightFields(
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
                ).build();

        Page<DiscussPost> page = elasticTemplate.queryForPage(searchQuery, DiscussPost.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> aClass, Pageable pageable) {
                SearchHits hits = response.getHits();
                if (hits.getTotalHits() <= 0) {
                    return null;
                }
                List<DiscussPost> list = new ArrayList<>();
                for (SearchHit hit : hits) {
                    DiscussPost post = new DiscussPost();

                    String id = hit.getSourceAsMap().get("id").toString();
                    post.setId(Integer.valueOf(id));

                    String userId = hit.getSourceAsMap().get("userId").toString();
                    post.setUserId(Integer.valueOf(userId));

                    String title = hit.getSourceAsMap().get("title").toString();
                    post.setTitle(title);

                    String content = hit.getSourceAsMap().get("content").toString();
                    post.setContent(content);

                    String status = hit.getSourceAsMap().get("status").toString();
                    post.setStatus(Integer.valueOf(status));

                    String createTime = hit.getSourceAsMap().get("createTime").toString();
                    post.setCreateTime(new Date(Long.valueOf(createTime)));

                    String commentCount = hit.getSourceAsMap().get("commentCount").toString();
                    post.setCommentCount(Integer.valueOf(commentCount));

                    // 处理高亮显示的结果
                    HighlightField titleField = hit.getHighlightFields().get("title");
                    if (titleField != null) {
                        post.setTitle(titleField.getFragments()[0].toString());
                    }

                    HighlightField contentField = hit.getHighlightFields().get("content");
                    if (contentField != null) {
                        post.setContent(contentField.getFragments()[0].toString());
                    }

                    list.add(post);
                }

                return new AggregatedPageImpl(list, pageable,
                        hits.getTotalHits(), response.getAggregations(), response.getScrollId(), hits.getMaxScore());
            }

        });

        System.out.println(page.getTotalElements());
        System.out.println(page.getTotalPages());
        System.out.println(page.getNumber());
        System.out.println(page.getSize());
        for (DiscussPost post : page) {
            System.out.println(post);
        }
    }


//    @Test
//    public void highlightQuery() throws Exception {
//        //1.创建搜索请求
//        // searchRequest
//        SearchRequest searchRequest = new SearchRequest("discusspost");//discusspost是索引名，就是表名
//        // 2.配置高亮 HighlightBuilder
//        HighlightBuilder highlightBuilder = new HighlightBuilder();
//        highlightBuilder.field("title"); //为哪些字段匹配到的内容设置高亮
//        highlightBuilder.field("content");
//        highlightBuilder.requireFieldMatch(false);
//        highlightBuilder.preTags("<span style='color:red'>"); //相当于把结果套了一点html标签  然后前端获取到数据就直接用
//        highlightBuilder.postTags("</span>");
//        //3.构建搜索条件 searchSourceBuilder
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
//                .query(QueryBuilders.multiMatchQuery("互联网寒冬", "title", "content"))
//                .sort(SortBuilders.fieldSort("type").order(SortOrder.DESC))
//                .sort(SortBuilders.fieldSort("score").order(SortOrder.DESC))
//                .sort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
//                .from(0)// 指定从哪条开始查询
//                .size(10)// 需要查出的总记录条数
//                .highlighter(highlightBuilder);//配置高亮
//        // 4.将搜索条件参数传入搜索请求
//        searchRequest.source(searchSourceBuilder);
//        //5.使用客户端发送请求
//        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
//        List<DiscussPost> list = new LinkedList<>();
//        for (SearchHit hit : searchResponse.getHits().getHits()) {
//            DiscussPost discussPost = JSONObject.parseObject(hit.getSourceAsString(), DiscussPost.class);   // 处理高亮显示的结果
//            HighlightField titleField = hit.getHighlightFields().get("title");
//            if (titleField != null) {
//                discussPost.setTitle(titleField.getFragments()[0].toString());  //title=<span style='color:red'>互联网</span>求职暖春计划...  }
//                HighlightField contentField = hit.getHighlightFields().get("content");
//                if (contentField != null) {
//                    discussPost.setContent(contentField.getFragments()[0].toString());  //content=它是最时尚的<span style='color:red'>互联网</span>公司之一...  }
//                    System.out.println(discussPost);
//                    list.add(discussPost);
//                }
//            }
//        }
//    }
}
