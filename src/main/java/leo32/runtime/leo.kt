package leo32.runtime

import leo.base.*
import leo32.base.I32
import leo32.seq32
import leo32.string32

data class Leo(
	val reader: Reader,
	val errorI32OrNull: I32?)

val Reader.leo get() =
	Leo(this, null)

val Empty.leo get() =
	reader.leo

fun Leo.plus(i32: I32) =
	if (errorI32OrNull != null) this
	else reader
		.plus(i32)
		?.let { copy(reader = it) }
		?:copy(errorI32OrNull = i32)

val Leo.seq32 get() =
	reader.seq32.then {
		if (errorI32OrNull != null) flatSeq(errorI32OrNull.onlySeq, "<<<ERROR".seq32)
		else emptySeq()
	}

val String.leo get() =
	empty.leo.fold(seq32) { plus(it) }.seq32.string32

