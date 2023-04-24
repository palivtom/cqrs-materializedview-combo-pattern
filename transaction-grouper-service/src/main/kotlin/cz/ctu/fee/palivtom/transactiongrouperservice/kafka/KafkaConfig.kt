package cz.ctu.fee.palivtom.transactiongrouperservice.kafka

import com.fasterxml.jackson.databind.JsonNode
import cz.ctu.fee.palivtom.transactiongrouperservice.kafka.model.DebeziumPsqlTableValue
import cz.ctu.fee.palivtom.transactiongrouperservice.kafka.model.TransactionValue
import io.confluent.kafka.serializers.json.KafkaJsonSchemaDeserializerConfig
import mu.KotlinLogging
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.DefaultErrorHandler
import org.springframework.util.backoff.FixedBackOff

private val logger = KotlinLogging.logger {}

@EnableKafka
@Configuration
class KafkaConfig(
    private val defaultKafkaProperties: KafkaProperties
) {
    @Bean
    fun outputTopic(@Value("\${kafka.topics.output}") topicName: String): NewTopic {
        return TopicBuilder.name(topicName).build()
    }

    @Bean
    fun defaultKafkaErrorHandler(): DefaultErrorHandler {
        return DefaultErrorHandler(
            { record, ex -> logger.error { "An error has occur, record:\n$record\n$ex" } },
            FixedBackOff(0L, 0L)
        )
    }

    @Bean
    fun tableEventContainerFactory(): ConcurrentKafkaListenerContainerFactory<JsonNode, DebeziumPsqlTableValue> {
        return createConsumerContainerFactory(true)
    }

    @Bean
    fun transactionRecordContainerFactory(): ConcurrentKafkaListenerContainerFactory<JsonNode, TransactionValue> {
        return createConsumerContainerFactory(true)
    }

    private inline fun <reified K: Any, reified V: Any> createConsumerContainerFactory(
        allowUnknownProps: Boolean
    ): ConcurrentKafkaListenerContainerFactory<K, V> {
        return ConcurrentKafkaListenerContainerFactory<K, V>().apply {
            consumerFactory = DefaultKafkaConsumerFactory(
                HashMap(defaultKafkaProperties.buildConsumerProperties()).apply {
                    this[KafkaJsonSchemaDeserializerConfig.FAIL_UNKNOWN_PROPERTIES] = !allowUnknownProps
                    this[KafkaJsonSchemaDeserializerConfig.JSON_KEY_TYPE] = K::class.java.canonicalName
                    this[KafkaJsonSchemaDeserializerConfig.JSON_VALUE_TYPE] = V::class.java.canonicalName
                }
            )
        }
    }
}