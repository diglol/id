package diglol.id

import diglol.crypto.random.nextBytes
import diglol.id.internal.epochSeconds
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class IdTest {

  @Test
  fun generate() {
    Id.machine = nextBytes(3)
    val epochSeconds = epochSeconds()
    val id = Id.generate(epochSeconds)
    assertEquals(epochSeconds, id.time)
    assertContentEquals(Id.machine, id.machine)
  }

  @Test
  fun fromBytes() {
    val bytes = byteArrayOf(
      0x00, 0x4d,
      0x88.toByte(), 0xe1.toByte(), 0x5b, 0x60, 0xf4.toByte(),
      0x86.toByte(), 0xe4.toByte(), 0x28, 0x41, 0x2d, 0xc9.toByte()
    )
    val id = Id.fromBytes(bytes)
    assertEquals(id.toString(), "016ohoarc3q8dp1884msi")
  }

  @Test
  fun fromString() {
    val id = Id.fromString("016ohoarc3q8dp1884msi")
    val bytes = byteArrayOf(
      0x00, 0x4d,
      0x88.toByte(), 0xe1.toByte(), 0x5b, 0x60, 0xf4.toByte(),
      0x86.toByte(), 0xe4.toByte(), 0x28, 0x41, 0x2d, 0xc9.toByte()
    )
    assertContentEquals(bytes, id.bytes)
  }
}
