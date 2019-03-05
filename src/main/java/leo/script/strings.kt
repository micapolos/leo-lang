package leo.script

import leo.base.mapChars
import leo.base.utf8ByteArray

val String.scriptInvert
	get() =
		mapChars { it.scriptInvert }

val String.scriptBytes
	get() =
		scriptInvert.utf8ByteArray