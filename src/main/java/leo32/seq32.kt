package leo32

import leo.base.Seq
import leo.base.codePointSeq
import leo.base.map
import leo.base.onlySeq
import leo.binary.Bit

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
