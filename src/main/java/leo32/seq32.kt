package leo32

import leo.base.*
import leo.binary.Bit
import leo32.base.I32
import leo32.base.i32

typealias Seq32 = Seq<I32>

val I32.seq32
	get() =
		onlySeq

val Bit.seq32
	get() =
		i32.seq32

val Byte.seq32
	get() =
		i32.seq32

val Int.seq32
	get() =
		i32.seq32

val String.seq32
	get() =
		codePointSeq.map { i32 }

val Seq32.string32 get() =
	map { int }.codePointString