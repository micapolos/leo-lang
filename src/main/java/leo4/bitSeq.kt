package leo4

import leo.base.*
import leo.binary.Bit
import leo.binary.byteBitSeq
import leo.binary.utf8ByteSeq

// TODO: Escape zeros
val String.wordBitSeq: Seq<Bit>
	get() =
		string.utf8ByteSeq.then { byte(0).onlySeq }.byteBitSeq

val Line.bitSeq: Seq<Bit>
	get() = Seq {
		seqNodeOrNull(string.wordBitSeq, script.bitSeq)
	}

val Term.bitSeq: Seq<Bit>
	get() = when (this) {
		is LineTerm -> line.bitSeq
		is ApTerm -> ap.bitSeq
	}

val Ap.bitSeq: Seq<Bit>
	get() = Seq {
		seqNodeOrNull(term.bitSeq, line.bitSeq)
	}

val Script.bitSeq
	get() = when (this) {
		is EmptyScript -> emptySeq()
		is TermScript -> term.bitSeq
	}
