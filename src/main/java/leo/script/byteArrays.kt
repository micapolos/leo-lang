package leo.script

import leo.base.utf8String

val ByteArray.script
	get() =
		utf8String.scriptInvert