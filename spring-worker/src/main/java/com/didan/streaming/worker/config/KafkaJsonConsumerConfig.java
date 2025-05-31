package com.didan.streaming.worker.config;

import com.didan.streaming.worker.dto.VideoMessage;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.security.plain.PlainLoginModule;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.mapping.AbstractJavaTypeMapper;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@EnableKafka
@Configuration
@Slf4j
public class KafkaJsonConsumerConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  @Value("${spring.kafka.auth.username:#{null}}")
  private String authUsername;

  @Value("${spring.kafka.auth.password:#{null}}")
  private String authPassword;

  @Bean
  public ConsumerFactory<String, Object> jsonConsumerFactory() {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
    props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, CustomJsonDeserializer.class);
    addAuth(props);

    return new DefaultKafkaConsumerFactory<>(props);
  }

  private void addAuth(Map<String, Object> props) {
    if (this.authUsername != null && this.authPassword != null) {
      props.put("sasl.jaas.config",
          String.format("%s required username=\"%s\" password=\"%s\";", PlainLoginModule.class.getName(), this.authUsername, this.authPassword));
      props.put("sasl.mechanism", "PLAIN");
      props.put("security.protocol", "SASL_PLAINTEXT");
    }
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, VideoMessage> kafkaJsonListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, VideoMessage> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(jsonConsumerFactory());
    return factory;
  }
  
  public static class CustomJsonDeserializer extends JsonDeserializer<Object> {
    public CustomJsonDeserializer() {
      this.typeMapper.addTrustedPackages("*");
      ((AbstractJavaTypeMapper) this.getTypeMapper()).setUseForKey(true);
    }
  }
}
