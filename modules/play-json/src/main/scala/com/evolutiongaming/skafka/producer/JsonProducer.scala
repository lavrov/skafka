package com.evolutiongaming.skafka.producer

import cats.Applicative
import com.evolutiongaming.catshelper.FromTry
import com.evolutiongaming.skafka.{ToBytes, Topic}
import play.api.libs.json.{JsValue, Json}

trait JsonProducer[F[_]] {
  def apply(record: ProducerRecord[String, JsValue]): F[F[RecordMetadata]]
}

object JsonProducer {

  def empty[F[_] : Applicative : FromTry]: JsonProducer[F] = apply(Producer.Send.empty[F])

  implicit def jsValueToBytes[F[_] : FromTry]: ToBytes[F, JsValue] = {
    (value: JsValue, _: Topic) => FromTry[F].unsafe { Json.toBytes(value) }
  }

  def apply[F[_] : FromTry](send: Producer.Send[F]): JsonProducer[F] = (record: ProducerRecord[String, JsValue]) => send(record)
}