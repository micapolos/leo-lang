package leo3

import leo.base.Seq
import leo.base.empty
import leo.binary.Bit

data class LineReader(
	val value: Value)

fun lineReader(value: Value) =
	LineReader(value)

val Value.lineReader
	get() = lineReader(this)

val LineReader.begin
	get() = lineReader(value(empty))

fun LineReader.plus(line: Line): LineReader =
	copy(value = value.plus(line))

val LineReader.bitSeq: Seq<Bit>
	get() = value.bitSeq

fun Appendable.append(lineReader: LineReader): Appendable =
	append(lineReader.value)