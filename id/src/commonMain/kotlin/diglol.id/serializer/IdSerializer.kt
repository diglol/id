package diglol.id.serializer

import diglol.id.Id
import diglol.id.decodeToId
import diglol.id.toId
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ByteArraySerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

// TODO remove https://github.com/Kotlin/kotlinx.serialization/issues/1289
object IdSerializer : KSerializer<Id> {
  override val descriptor: SerialDescriptor = buildClassSerialDescriptor("diglol.id.Id")

  override fun deserialize(decoder: Decoder): Id {
    return if (decoder::class.qualifiedName?.startsWith("kotlinx.serialization.protobuf") == true)
      decoder.decodeSerializableValue(ByteArraySerializer()).toId() else decoder.decodeString()
      .decodeToId()
  }

  override fun serialize(encoder: Encoder, value: Id) {
    return if (encoder::class.qualifiedName?.startsWith("kotlinx.serialization.protobuf") == true)
      encoder.encodeSerializableValue(ByteArraySerializer(), value.bytes) else
      encoder.encodeString(value.toString())
  }
}
