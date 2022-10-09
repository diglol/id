package diglol.id

import diglol.crypto.random.nextBytes
import diglol.crypto.random.nextInt
import diglol.id.Id.Companion.decodeToId
import diglol.id.Id.Companion.empty
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
  fun padding() {
    val encode = byteArrayOf(
      48, 49, 50, 51, 52, 53, 54, 55, 56, 57, // 0-9
      97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115,
      116, 117, 118 // a-v
    )
    repeat(10000) {
      val id3 = ByteArray(21)
      id3[20] = 48 // "0"
      "016ohoarc3q8dp".encodeToByteArray().copyInto(id3, 0, 0) // 016ohoarc3q8dp1884ms0
      repeat(6) {
        id3[14 + it] = encode[nextInt(32)]
      }
      val id3String = id3.decodeToString()
      assertEquals(id3String, id3String.decodeToId().encodeToString())
    }
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

    val expected = "016ohoarc3q8dp1884ms1".decodeToId() // invalid
    assertEquals(expected, empty)
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
