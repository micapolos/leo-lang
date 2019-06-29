package leo7

import leo.base.*
import leo.binary.bitSeq

val Letter.bitSeq
	get() =
		char.toByte().bitSeq

val Word.bitSeq
	get() =
		letterStack.seq.map { bitSeq }.flat

val WordBegin.bitSeq
	get() =
		word.bitSeq.then(0.toByte().bitSeq)

@Suppress("unused")
val End.bitSeq
	get() =
		0.toByte().bitSeq

val Token.bitSeq
	get() =
		switch({ bitSeq }, { bitSeq })

val Script.bitSeq
	get() =
		tokenSeq.mapFlat { bitSeq }

val Line.bitSeq
	get() =
		tokenSeq.mapFlat { bitSeq }
