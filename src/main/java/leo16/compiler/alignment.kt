package leo16.compiler

import leo16.invoke
import leo16.names.*
import kotlin.math.max

enum class Alignment {
	ALIGNMENT_1,
	ALIGNMENT_2,
	ALIGNMENT_4,
	ALIGNMENT_8;

	override fun toString() = asField.toString()
}

val Alignment.asField get() = _alignment(size.toString()())

val alignments = Alignment.values().toList()
val indexAlignment = Alignment.ALIGNMENT_4

val Alignment.size: Int
	get() =
		1.shl(ordinal)

infix fun Alignment.and(alignment: Alignment): Alignment =
	alignments[max(ordinal, alignment.ordinal)]

val Alignment.mask: Int
	get() =
		size - 1

fun Alignment.align(int: Int): Int =
	if (int.and(mask) == 0) int
	else int.and(mask.inv()).plus(size)