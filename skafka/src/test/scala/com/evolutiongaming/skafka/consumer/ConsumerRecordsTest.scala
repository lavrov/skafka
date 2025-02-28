package com.evolutiongaming.skafka.consumer

import cats.data.{NonEmptyList => Nel}
import cats.implicits._
import com.evolutiongaming.skafka.{Offset, Partition, Topic, TopicPartition}
import org.scalatest.{FunSuite, Matchers}

class ConsumerRecordsTest extends FunSuite with Matchers {

  test("summaryShow") {
    def consumerRecord(topic: Topic, partition: Partition, offset: Offset, key: Int) = {
      ConsumerRecord(
        topicPartition = TopicPartition(topic, partition),
        offset = offset,
        timestampAndType = none,
        key = Some(WithSize(key)),
        value = none[WithSize[Nothing]],
        headers = Nil)
    }

    val records = Nel.of(
      consumerRecord(topic = "0", partition = 0, offset = 1, key = 0),
      consumerRecord(topic = "0", partition = 0, offset = 2, key = 1),
      consumerRecord(topic = "0", partition = 0, offset = 4, key = 2),
      consumerRecord(topic = "0", partition = 0, offset = 0, key = 0),
      consumerRecord(topic = "0", partition = 0, offset = 3, key = 0),
      consumerRecord(topic = "0", partition = 1, offset = 1, key = 11),
      consumerRecord(topic = "0", partition = 2, offset = 0, key = 21),
      consumerRecord(topic = "1", partition = 0, offset = 1, key = 0),
      consumerRecord(topic = "1", partition = 0, offset = 0, key = 0),
      consumerRecord(topic = "0", partition = 1, offset = 0, key = 11))

    val consumerRecords = ConsumerRecords(records.sorted.groupBy(_.topicPartition))

    ConsumerRecords.summaryShow.show(consumerRecords) shouldEqual "0-0:0..4 records: 5, 0-1:0..1 records: 2, 0-2:0 records: 1, 1-0:0..1 records: 2"
  }
}
