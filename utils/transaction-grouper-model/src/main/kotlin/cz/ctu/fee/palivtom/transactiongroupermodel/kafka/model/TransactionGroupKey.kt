package cz.ctu.fee.palivtom.transactiongroupermodel.kafka.model

import com.fasterxml.jackson.annotation.JsonProperty

data class TransactionGroupKey(
   @JsonProperty("txId", required = true)
   val txId: String
)