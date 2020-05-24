package leo16.compiler

import leo16.invoke
import leo16.names.*
import kotlin.math.max

enum class Alignment {
	ALIGNMENT_1,
	ALIGNMENT_2,
	ALIGNMENT_4,
	ALIGNMENT_8,
	ALIGNMENT_16,
	ALIGNMENT_32,
	ALIGNMENT_64;

	override fun toString() = asField.toString()
}

val Alignment.asField get() = _alignment(bitCount.toString()())

val alignments = Alignment.values().toList()
val pointerAlignment = Alignment.ALIGNMENT_64

val Alignment.bitCount: Int
	get() =
		ordinal

infix fun Alignment.and(alignment: Alignment): Alignment =
	alignments[max(ordinal, alignment.ordinal)]

val Long.sizeAlignmentOrNull: Alignment?
	get() =
		when {
			this <= 0x1L -> null
			this <= 0x2L -> Alignment.ALIGNMENT_1
			this <= 0x4L -> Alignment.ALIGNMENT_2
			this <= 0x10L -> Alignment.ALIGNMENT_4
			this <= 0x100L -> Alignment.ALIGNMENT_8
			this <= 0x10000L -> Alignment.ALIGNMENT_16
			this <= 0x100000000L -> Alignment.ALIGNMENT_32
			else -> Alignment.ALIGNMENT_64
		}

val Alignment.indexMask: Int
	get() =
		1.shl(bitCount).dec()