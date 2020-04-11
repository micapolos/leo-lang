package leo14.untyped.typed

import leo.base.*
import leo.binary.seq
import leo13.base.Bit
import leo13.base.bit0
import leo13.base.bit1
import leo13.base.bit2
import leo13.base.bit3
import leo13.base.bit4
import leo13.base.bit5
import leo13.base.bit6
import leo13.base.bit7
import leo14.*

val Seq<Script>.script: Script
	get() =
		leo("list" lineTo script().fold(this) { plus(it) })

val Bit.script: Script
	get() =
		leo("bit"(if (isOne) "one"() else "zero"()))

val Boolean.script: Script
	get() =
		leo("boolean"(if (this) "true"() else "false"()))

val Byte.script: Script
	get() =
		leo("byte"(
			"first" lineTo bit7.script,
			"second" lineTo bit6.script,
			"third" lineTo bit5.script,
			"fourth" lineTo bit4.script,
			"fifth" lineTo bit3.script,
			"sixth" lineTo bit2.script,
			"seventh" lineTo bit1.script,
			"eight" lineTo bit0.script))

val Int.script: Script
	get() =
		leo("int"(
			"first" lineTo byte3.script,
			"second" lineTo byte2.script,
			"third" lineTo byte1.script,
			"fourth" lineTo byte0.script))

val ByteArray.script: Script
	get() =
		leo("byte" lineTo seq.map { script }.script)

val String.script: Script
	get() =
		leo("text"("utf8" lineTo utf8ByteArray.script))
