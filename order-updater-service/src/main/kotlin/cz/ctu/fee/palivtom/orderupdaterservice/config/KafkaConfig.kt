package cz.ctu.fee.palivtom.orderupdaterservice.config

import com.fasterxml.jackson.databind.JsonNode
import cz.ctu.fee.palivtom.orderupdaterservice.model.kafka.TransactionRecordValue
import io.confluent.kafka.serializers.json.KafkaJsonSchemaDeserializerConfig
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.DefaultKafkaConsumerFactory

@EnableKafka
@Configuration
class KafkaConfig(
    private val defaultKafkaProperties: KafkaProperties
) {
    @Bean
    fun transactionStatusTopic(@Value("\${kafka.topics.event.transaction.status}") topicName: String): NewTopic {
        return TopicBuilder.name(topicName).build()
    }

    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<JsonNode, JsonNode> {
        return createConsumerContainerFactory(true)
    }

    @Bean
    fun transactionRecordContainerFactory(): ConcurrentKafkaListenerContainerFactory<JsonNode, TransactionRecordValue> {
        return createConsumerContainerFactory(true)
    }

    /**
     * Container factory - be aware of generic types, it is not supported.
     */
    private inline fun <reified K: Any, reified V: Any> createConsumerContainerFactory(
        allowUnknownProps: Boolean
    ): ConcurrentKafkaListenerContainerFactory<K, V> {
        return ConcurrentKafkaListenerContainerFactory<K, V>().apply {
            consumerFactory = DefaultKafkaConsumerFactory(
                HashMap(defaultKafkaProperties.buildConsumerProperties()).apply {
                    this[KafkaJsonSchemaDeserializerConfig.FAIL_UNKNOWN_PROPERTIES] = !allowUnknownProps
                    this[KafkaJsonSchemaDeserializerConfig.JSON_KEY_TYPE] = K::class.java.name
                    this[KafkaJsonSchemaDeserializerConfig.JSON_VALUE_TYPE] = V::class.java.name
                }
            )
        }
    }
}