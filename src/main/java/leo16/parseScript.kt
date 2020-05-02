package leo16

import leo.base.int
import leo.base.short
import leo.base.utf8String
import leo13.*
import leo13.base.Bit
import leo13.base.byte
import leo13.base.oneBit
import leo13.base.zeroBit
import leo13.bitName
import leo14.Literal
import leo14.literal
import leo15.*
import leo15.byteName
import leo15.listName
import leo15.oneName
import leo15.zeroName

val Sentence.bitOrNull: Bit?
	get() =
		field.matchPrefix(bitName) { rhs ->
			rhs.matchWord { word ->
				when (word) {
					zeroName -> zeroBit
					oneName -> oneBit
					else -> null
				}
			}
		}

val Sentence.byteOrNull: Byte?
	get() =
		field.matchPrefix(byteName) { rhs ->
			rhs.matchPrefix(bitName) { rhs ->
				rhs.fieldStack.onlyOrNull
					?.sentenceOrNull
					?.parseSentenceStackOrNull { bitOrNull }
					?.array
					?.let { byte(it[0], it[1], it[2], it[3], it[4], it[5], it[6], it[7]) }
			}
		}

val Sentence.intOrNull: Int?
	get() =
		field.matchPrefix(intName) { rhs ->
			rhs.matchPrefix(byteName) { rhs ->
				rhs.fieldStack.onlyOrNull
					?.sentenceOrNull
					?.parseSentenceStackOrNull { byteOrNull }
					?.array
					?.let { int(short(it[0], it[1]), short(it[2], it[3])) }
			}
		}

val Sentence.stringOrNull: String?
	get() =
		field.matchPrefix(stringName) { rhs ->
			rhs.fieldStack.onlyOrNull
				?.sentenceOrNull
				?.parseSentenceStackOrNull { byteOrNull }
				?.array
				?.toByteArray()
				?.utf8String
		}

val Sentence.parseSentenceStackOrNull: Stack<Field>?
	get() =
		field.matchPrefix(listName) { it.fieldStack }

fun <T : Any> Sentence.parseSentenceStackOrNull(fn: Field.() -> T?): Stack<T>? =
	parseSentenceStackOrNull?.mapOrNull(fn)

val Sentence.literalOrNull: Literal?
	get() =
		null
			?: intOrNull?.literal
			?: stringOrNull?.literal