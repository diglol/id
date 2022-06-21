package diglol.id

import diglol.crypto.rand.nextBytes
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlinx.datetime.Clock

class IdTest {
  @Test
  fun testGenerate() {
    Id.generate()
  }

  @Test
  fun testGenerateWithTime() {
    Id.generate(Clock.System.now())
  }

  @Test
  fun testGenerateWithMachine() {
    Id.machine = nextBytes(3)
    val id1 = Id.generate()
    val id2 = Id.generate()
    assertContentEquals(id1.machine, id2.machine)
  }

  @Test
  fun testFromBytes() {
    val bytes =
      byteArrayOf(
        0x00, 0x4d,
        0x88.toByte(), 0xe1.toByte(), 0x5b, 0x60, 0xf4.toByte(),
        0x86.toByte(), 0xe4.toByte(), 0x28, 0x41, 0x2d, 0xc9.toByte()
      )
    val id = Id.fromBytes(bytes)
    assertEquals(id.toString(), "016ohoarc3q8dp1884msi")
  }

  @Test
  fun testFromString() {
    val id = Id.fromString("016ohoarc3q8dp1884msi")
    val bytes =
      byteArrayOf(
        0x00, 0x4d,
        0x88.toByte(), 0xe1.toByte(), 0x5b, 0x60, 0xf4.toByte(),
        0x86.toByte(), 0xe4.toByte(), 0x28, 0x41, 0x2d, 0xc9.toByte()
      )
    assertContentEquals(bytes, id.bytes)
  }
}
