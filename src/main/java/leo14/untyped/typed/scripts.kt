package leo14.untyped.typed

import leo.base.*
import leo.binary.Bit
import leo.binary.bitSeq
import leo.binary.isZero
import leo.binary.seq
import leo14.*

val Seq<Script>.script: Script
	get() =
		leo("list" lineTo script().fold(seq) { plus(it) })

val Bit.script: Script
	get() =
		leo("bit"(if (isZero) "zero"() else "one"()))

val Byte.script: Script
	get() =
		leo("byte"("bit" lineTo bitSeq.map { script }.script))

val Int.script: Script
	get() =
		leo("int"("byte" lineTo seq(byte3, byte2, byte1, byte0).map { script }.script))

val ByteArray.script: Script
	get() =
		leo("byte" lineTo seq.map { script }.script)

val String.script: Script
	get() =
		leo("text"("utf8" lineTo utf8ByteArray.script))
