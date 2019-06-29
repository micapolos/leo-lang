package leo7

import leo.base.*

val Script.tokenSeq: Seq<Token>
	get() =
		lineStackOrNull.seq.mapFlat { tokenSeq }

val Line.tokenSeq
	get() =
		flatSeq(
			word.begin.token.onlySeq,
			script.tokenSeq,
			end.token.onlySeq)
