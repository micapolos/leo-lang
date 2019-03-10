package leo.script

import leo.*
import leo.base.EnumBit
import leo.base.bitSequence
import leo.base.int
import leo.base.utf8ByteArray

val Boolean.scriptLine
	get() =
		booleanWord lineTo nullScript.plus(word)

val EnumBit.scriptLine
	get() =
		bitWord lineTo nullScript.plus(word)

val Byte.scriptLine
	get() =
		byteWord lineTo int.bitSequence(8).fold(nullScript) { script, bit ->
			script.plus(bit.scriptLine)
		}

val Int.scriptLine
	get() =
		byteWord lineTo bitSequence(32).fold(nullScript) { script, bit ->
			script.plus(bit.scriptLine)
		}

val String.scriptLine
	get() =
		stringWord lineTo nullScript.plus(
			utfWord lineTo nullScript.plus(
				eightWord lineTo utf8ByteArray.fold(nullScript) { script, byte ->
					script.plus(byte.scriptLine)
				}))