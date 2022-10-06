package diglol.id

import diglol.crypto.random.nextBytes
import diglol.id.Id.Companion.decodeToId
import diglol.id.Id.Companion.toId
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertTrue

class IdTest {

  @Test
  fun setMachine() {
    assertFails {
      Id.machine = nextBytes(2)
    }
  }

  @Test
  fun resetMachine() {
    val machine = Id.machine
    Id.machine = nextBytes(3)
    assertContentEquals(machine, Id.machine)
  }

  @Test
  fun generate() {
    val id1 = Id.generate()
    val id2 = Id.generate()
    assertEquals(id1.pid, id2.pid)
    assertContentEquals(id1.machine, id2.machine)
  }

  @Test
  fun generateByEpochSeconds() {
    val epochSeconds = epochSeconds()
    val id = Id.generate(epochSeconds)
    assertEquals(epochSeconds, id.time)
  }

  @Test
  fun toId() {
    val bytes = byteArrayOf(
      0x00, 0x4d,
      0x88.toByte(), 0xe1.toByte(), 0x5b, 0x60, 0xf4.toByte(),
      0x86.toByte(), 0xe4.toByte(), 0x28, 0x41, 0x2d, 0xc9.toByte()
    )
    val id = bytes.toId()
    assertEquals(id.encodeToString(), "016ohoarc3q8dp1884msi")
  }

  @Test
  fun decodeToId() {
    val id = "016ohoarc3q8dp1884msi".decodeToId()
    val bytes = byteArrayOf(
      0x00, 0x4d,
      0x88.toByte(), 0xe1.toByte(), 0x5b, 0x60, 0xf4.toByte(),
      0x86.toByte(), 0xe4.toByte(), 0x28, 0x41, 0x2d, 0xc9.toByte()
    )
    assertContentEquals(bytes, id.bytes)
  }

  @Test
  fun compareTo() {
    assertTrue(Id.generate() < Id.generate())
  }

  @Test
  fun emptyId() {
    assertEquals(Id.empty.encodeToString().decodeToId(), Id.empty)
  }
}
