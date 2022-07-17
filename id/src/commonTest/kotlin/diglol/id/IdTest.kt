package diglol.id

import diglol.crypto.random.nextBytes
import diglol.id.internal.epochSeconds
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse

class IdTest {

  @Test
  fun generate() {
    assertContentEquals(Id.generate().machine, Id.generate().machine)
    assertEquals(Id.generate().pid, Id.generate().pid)

    assertFails {
      Id.machine = nextBytes(2)
    }

    val machine = nextBytes(3)
    Id.machine = machine
    assertFalse(machine.contentEquals(Id.machine))

    val epochSeconds = epochSeconds()
    val id = Id.generate(epochSeconds)
    assertEquals(epochSeconds, id.time)
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
