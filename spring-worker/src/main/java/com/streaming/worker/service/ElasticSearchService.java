package com.streaming.worker.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ElasticSearchService {

    private final RestHighLevelClient elasticsearchClient;
    private final ObjectMapper objectMapper;

    private static final String VIDEO_INDEX = "videos";
    private static final String LIVESTREAM_INDEX = "livestreams";

    public void indexVideo(JsonNode video) {
        try {
            IndexRequest request = new IndexRequest(VIDEO_INDEX)
                    .id(video.get("id").asText())
                    .source(objectMapper.writeValueAsString(video), XContentType.JSON);
            
            elasticsearchClient.index(request, RequestOptions.DEFAULT);
            log.info("Indexed video document: {}", video.get("id").asText());
        } catch (Exception e) {
            log.error("Error indexing video document: {}", e.getMessage(), e);
        }
    }

    public void deleteVideo(String id) {
        try {
            DeleteRequest request = new DeleteRequest(VIDEO_INDEX, id);
            elasticsearchClient.delete(request, RequestOptions.DEFAULT);
            log.info("Deleted video document: {}", id);
        } catch (Exception e) {
            log.error("Error deleting video document: {}", e.getMessage(), e);
        }
    }

    public void indexLivestream(JsonNode livestream) {
        try {
            IndexRequest request = new IndexRequest(LIVESTREAM_INDEX)
                    .id(livestream.get("id").asText())
                    .source(objectMapper.writeValueAsString(livestream), XContentType.JSON);
            
            elasticsearchClient.index(request, RequestOptions.DEFAULT);
            log.info("Indexed livestream document: {}", livestream.get("id").asText());
        } catch (Exception e) {
            log.error("Error indexing livestream document: {}", e.getMessage(), e);
        }
    }

    public void deleteLivestream(String id) {
        try {
            DeleteRequest request = new DeleteRequest(LIVESTREAM_INDEX, id);
            elasticsearchClient.delete(request, RequestOptions.DEFAULT);
            log.info("Deleted livestream document: {}", id);
        } catch (Exception e) {
            log.error("Error deleting livestream document: {}", e.getMessage(), e);
        }
    }
} 