package diglol.id.serializer

import diglol.id.Id
import diglol.id.Id.Companion.decodeToId
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

// TODO remove https://github.com/Kotlin/kotlinx.serialization/issues/1289
object IdSerializer : KSerializer<Id> {
  override val descriptor: SerialDescriptor = buildClassSerialDescriptor("diglol.id.Id")

  override fun deserialize(decoder: Decoder): Id = decoder.decodeString().decodeToId()

  override fun serialize(encoder: Encoder, value: Id) = encoder.encodeString(value.toString())
}
