package cz.ctu.fee.palivtom.orderupdaterservice.kafka

import cz.ctu.fee.palivtom.transactiongroupermodel.kafka.model.TransactionGroupKey
import cz.ctu.fee.palivtom.transactiongroupermodel.kafka.model.TransactionGroupValue
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
    fun transactionStatusTopic(@Value("\${kafka.topics.updater-status}") topicName: String): NewTopic {
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
    fun transactionGrouperContainerFactory(): ConcurrentKafkaListenerContainerFactory<TransactionGroupKey, TransactionGroupValue> {
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