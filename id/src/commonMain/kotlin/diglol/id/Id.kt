package diglol.id

import diglol.crypto.random.nextBytes
import diglol.id.serializer.IdSerializer
import kotlin.jvm.JvmField
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic
import kotlin.native.CName
import kotlin.experimental.ExperimentalNativeApi
import kotlinx.serialization.Serializable

@Serializable(with = IdSerializer::class)
class Id private constructor(private val raw: ByteArray) : Comparable<Id> {
  val bytes: ByteArray get() = raw.copyOf()

  // big endian
  val time
    get() = raw[0].toLong() and 0xff shl 32 or
      (raw[1].toLong() and 0xff shl 24) or
      (raw[2].toLong() and 0xff shl 16) or
      (raw[3].toLong() and 0xff shl 8) or
      (raw[4].toLong() and 0xff)

  val machine get() = raw.copyOfRange(5, 8)

  // big endian
  val pid get() = (raw[8].toInt() and 0xff shl 8) or (raw[9].toInt() and 0xff)

  // big endian
  val counter
    get() = (raw[9].toInt() and 0xff shl 16) or (raw[10].toInt() and 0xff shl 8) or
      (raw[11].toInt() and 0xff)

  fun isEmpty() = this == empty

  fun isNotEmpty() = this != empty

  fun encodeToString() = encode(raw).decodeToString()

  override fun toString(): String = encodeToString()

  override fun compareTo(other: Id): Int {
    for (i in 0 until rawSize) {
      val b = raw[i]
      val ob = other.raw[i]
      if (b != ob) {
        return if (b.toInt() and 0xff < ob.toInt() and 0xff) -1 else 1
      }
    }
    return 0
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || this::class != other::class) return false

    other as Id

    if (!raw.contentEquals(other.raw)) return false

    return true
  }

  override fun hashCode(): Int = raw.contentHashCode()

  companion object {
    private const val encodedSize = 21
    private const val rawSize = 13
    private val pid: Int = readPid()
    private var _machine: ByteArray? = null
    var machine: ByteArray
      // Private Getter TODO https://youtrack.jetbrains.com/issue/KT-3110
      get() = _machine ?: (nextBytes(3)).also { _machine = it }
      @JvmStatic
      set(value) {
        if (value.size < 3) {
          throw Error("The machine size in bytes must be >= 3")
        }
        if (_machine == null) {
          this._machine = value
        }
      }

    private val decode = intArrayOf(
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      0, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24,
      25, 26, 27, 28, 29, 30, 31, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
      -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1
    )
    private val encode = byteArrayOf(
      48, 49, 50, 51, 52, 53, 54, 55, 56, 57, // 0-9
      97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115,
      116, 117, 118 // a-v
    )

    @JvmField
    val empty = Id(ByteArray(rawSize))

    private fun encode(raw: ByteArray): ByteArray {
      val dst = ByteArray(encodedSize)
      val rawInts = IntArray(rawSize) { index ->
        raw[index].toInt() and 0xff // big endian
      }

      dst[20] = encode[(rawInts[12] shl 1) and 0x1f]
      dst[19] = encode[(rawInts[12] shr 4) or (rawInts[11] shl 4) and 0x1f]
      dst[18] = encode[(rawInts[11] shr 1) and 0x1f]
      dst[17] = encode[(rawInts[11] shr 6) or (rawInts[10] shl 2) and 0x1f]
      dst[16] = encode[rawInts[10] shr 3]
      dst[15] = encode[rawInts[9] and 0x1f]
      dst[14] = encode[(rawInts[9] shr 5) or (rawInts[8] shl 3) and 0x1f]
      dst[13] = encode[(rawInts[8] shr 2) and 0x1f]
      dst[12] = encode[rawInts[8] shr 7 or (rawInts[7] shl 1) and 0x1f]
      dst[11] = encode[(rawInts[7] shr 4) or (rawInts[6] shl 4) and 0x1f]
      dst[10] = encode[(rawInts[6] shr 1) and 0x1f]
      dst[9] = encode[(rawInts[6] shr 6) or (rawInts[5] shl 2) and 0x1f]
      dst[8] = encode[rawInts[5] shr 3]
      dst[7] = encode[rawInts[4] and 0x1f]
      dst[6] = encode[rawInts[4] shr 5 or (rawInts[3] shl 3) and 0x1f]
      dst[5] = encode[(rawInts[3] shr 2) and 0x1f]
      dst[4] = encode[rawInts[3] shr 7 or (rawInts[2] shl 1) and 0x1f]
      dst[3] = encode[(rawInts[2] shr 4) or (rawInts[1] shl 4) and 0x1f]
      dst[2] = encode[(rawInts[1] shr 1) and 0x1f]
      dst[1] = encode[(rawInts[1] shr 6) or (rawInts[0] shl 2) and 0x1f]
      dst[0] = encode[rawInts[0] shr 3]
      return dst
    }

    private fun decode(src: ByteArray): Id {
      val srcInts = IntArray(encodedSize) { index ->
        val v = src[index].toInt()
        if (decode[v] == -1) {
          return empty
        }
        v
      }
      val raw = ByteArray(rawSize)
      raw[12] = ((decode[srcInts[19]] shl 4) or (decode[srcInts[20]] shr 1)).toByte()
      if (encode[(raw[12].toInt() and 0xff shl 1) and 0x1f] != src[20]) {
        return empty
      }
      raw[11] =
        ((decode[srcInts[17]] shl 6) or (decode[srcInts[18]] shl 1) or (decode[srcInts[19]] shr 4)).toByte()
      raw[10] = ((decode[srcInts[16]] shl 3) or (decode[srcInts[17]] shr 2)).toByte()
      raw[9] = ((decode[srcInts[14]] shl 5) or decode[srcInts[15]]).toByte()
      raw[8] =
        ((decode[srcInts[12]] shl 7) or (decode[srcInts[13]] shl 2) or (decode[srcInts[14]] shr 3)).toByte()
      raw[7] = ((decode[srcInts[11]] shl 4) or (decode[srcInts[12]] shr 1)).toByte()
      raw[6] =
        ((decode[srcInts[9]] shl 6) or (decode[srcInts[10]] shl 1) or (decode[srcInts[11]] shr 4)).toByte()
      raw[5] = ((decode[srcInts[8]] shl 3) or (decode[srcInts[9]] shr 2)).toByte()
      raw[4] = ((decode[srcInts[6]] shl 5) or decode[srcInts[7]]).toByte()
      raw[3] =
        ((decode[srcInts[4]] shl 7) or (decode[srcInts[5]] shl 2) or (decode[srcInts[6]] shr 3)).toByte()
      raw[2] = ((decode[srcInts[3]] shl 4) or (decode[srcInts[4]] shr 1)).toByte()
      raw[1] =
        ((decode[srcInts[1]] shl 6) or (decode[srcInts[2]] shl 1) or (decode[srcInts[3]] shr 4)).toByte()
      raw[0] = ((decode[srcInts[0]] shl 3) or (decode[srcInts[1]] shr 2)).toByte()
      return Id(raw)
    }

    private fun generateRaw(epochSeconds: Long): ByteArray {
      val counter = counter()
      val raw = ByteArray(rawSize)
      raw[0] = (epochSeconds shr 32).toByte()
      raw[1] = (epochSeconds shr 24).toByte()
      raw[2] = (epochSeconds shr 16).toByte()
      raw[3] = (epochSeconds shr 8).toByte()
      raw[4] = epochSeconds.toByte()
      val machine = machine
      raw[5] = machine[0]
      raw[6] = machine[1]
      raw[7] = machine[2]
      raw[8] = (pid shr 8).toByte()
      raw[9] = pid.toByte()
      raw[10] = (counter shr 16).toByte()
      raw[11] = (counter shr 8).toByte()
      raw[12] = counter.toByte()
      return raw
    }

    @JvmStatic
    @JvmOverloads
    fun generate(epochSeconds: Long = epochSeconds()): Id = Id(generateRaw(epochSeconds))


    @OptIn(ExperimentalNativeApi::class)
    @JvmStatic
    @JvmName("of")
    @CName("of")
    fun ByteArray.toId() = if (size != rawSize) empty else Id(this)


    @OptIn(ExperimentalNativeApi::class)
    @JvmStatic
    @JvmName("decodeString")
    @CName("decodeString")
    fun String.decodeToId() = if (length != encodedSize) empty else decode(encodeToByteArray())
  }
}
