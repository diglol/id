package diglol.id

import diglol.crypto.random.nextInt

@Suppress("USELESS_ELVIS") // test
internal actual fun readPid(): Int = android.os.Process.myPid() ?: nextInt()
