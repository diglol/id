package diglol.id.internal

import diglol.crypto.random.nextInt

@Suppress("USELESS_ELVIS") // test
actual fun readPid(): Int = android.os.Process.myPid() ?: nextInt()
