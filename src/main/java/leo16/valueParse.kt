package leo16

import leo.base.The
import leo.base.int
import leo.base.short
import leo.base.utf8String
import leo13.Stack
import leo13.array
import leo13.base.Bit
import leo13.base.byte
import leo13.base.oneBit
import leo13.base.zeroBit
import leo13.linkOrNull
import leo13.map
import leo13.mapOrNull
import leo13.onlyOrNull
import leo13.push
import leo13.reverse
import leo13.stack
import leo14.Literal
import leo14.literal
import leo16.names.*

val Sentence.bitOrNull: Bit?
	get() =
		field.matchPrefix(_bit) { rhs ->
			rhs.matchWord { word ->
				when (word) {
					_zero -> zeroBit
					_one -> oneBit
					else -> null
				}
			}
		}

val Sentence.byteOrNull: Byte?
	get() =
		field.matchPrefix(_byte) { rhs ->
			rhs.matchPrefix(_bit) { rhs ->
				rhs.fieldStack.onlyOrNull
					?.sentenceOrNull
					?.listOrNull { bitOrNull }
					?.array
					?.let { byte(it[0], it[1], it[2], it[3], it[4], it[5], it[6], it[7]) }
			}
		}

val Sentence.intOrNull: Int?
	get() =
		field.matchPrefix(_int) { rhs ->
			rhs.matchPrefix(_byte) { rhs ->
				rhs.fieldStack.onlyOrNull
					?.sentenceOrNull
					?.listOrNull { byteOrNull }
					?.array
					?.let { int(short(it[0], it[1]), short(it[2], it[3])) }
			}
		}

val Sentence.stringOrNull: String?
	get() =
		field.matchPrefix(_string) { rhs ->
			rhs.fieldStack.onlyOrNull
				?.sentenceOrNull
				?.listOrNull { byteOrNull }
				?.array
				?.toByteArray()
				?.utf8String
		}

val Sentence.fieldListOrNull: Stack<Value>?
	get() =
		field.matchPrefix(_list) { rhs ->
			if (rhs == value(_empty())) stack()
			else rhs.fieldStack.linkOrNull?.let { stack(it) }?.mapOrNull {
				matchPrefix(_item) { it }
			}
		}

fun <T : Any> Sentence.listOrNull(fn: Value.() -> T?): Stack<T>? =
	fieldListOrNull?.mapOrNull(fn)

fun <T> Sentence.theListOrNull(fn: Value.() -> The<T>?): Stack<T>? =
	fieldListOrNull?.mapOrNull(fn)?.map { value }

val Value.listOrNull: Stack<Value>?
	get() =
		listOrNull { this }

fun <T : Any> Value.listOrNull(fn: Value.() -> T?): Stack<T>? =
	onlyFieldOrNull?.sentenceOrNull?.listOrNull(fn)

fun <T> Value.theListOrNull(fn: Value.() -> The<T>?): Stack<T>? =
	onlyFieldOrNull?.sentenceOrNull?.theListOrNull(fn)

val Sentence.literalOrNull: Literal?
	get() =
		null
			?: intOrNull?.literal
			?: stringOrNull?.literal

tailrec fun Stack<Value>.pushOrNull(field: Field): Stack<Value>? {
	val sentence = field.rhsOrNull(_list)?.onlyFieldOrNull?.sentenceOrNull ?: return null
	return when (sentence.word) {
		_empty ->
			if (sentence.value.isEmpty) this
			else null
		_link -> {
			val (lhs, last) = sentence.value.pairOrNull(_last) ?: return null
			val previous = lhs.rhsOrNull(_previous)?.onlyFieldOrNull ?: return null
			push(last).pushOrNull(previous)
		}
		else -> null
	}
}

val Field.stackOrNull: Stack<Value>?
	get() =
		stack<Value>().pushOrNull(this)?.reverse

val Value.stackOrNull: Stack<Value>?
	get() =
		onlyFieldOrNull?.stackOrNull

