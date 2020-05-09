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
import leo13.bitName
import leo13.linkOrNull
import leo13.map
import leo13.mapOrNull
import leo13.onlyOrNull
import leo13.push
import leo13.reverse
import leo13.stack
import leo14.Literal
import leo14.literal
import leo15.byteName
import leo15.intName
import leo15.listName
import leo15.oneName
import leo15.stringName
import leo15.zeroName
import leo16.names.*

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
					?.listOrNull { bitOrNull }
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
					?.listOrNull { byteOrNull }
					?.array
					?.let { int(short(it[0], it[1]), short(it[2], it[3])) }
			}
		}

val Sentence.stringOrNull: String?
	get() =
		field.matchPrefix(stringName) { rhs ->
			rhs.fieldStack.onlyOrNull
				?.sentenceOrNull
				?.listOrNull { byteOrNull }
				?.array
				?.toByteArray()
				?.utf8String
		}

val Sentence.fieldListOrNull: Stack<Value>?
	get() =
		field.matchPrefix(listName) { rhs ->
			if (rhs == value(leo15.emptyName())) stack()
			else rhs.fieldStack.linkOrNull?.let { stack(it) }?.mapOrNull {
				matchPrefix(leo15.itemName) { it }
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

fun Stack<Value>.pushOrNull(field: Field): Stack<Value>? =
	field.matchPrefix(_list) { rhs ->
		rhs.onlyFieldOrNull?.sentenceOrNull?.let { sentence ->
			when (sentence.word) {
				_empty -> this
				_link -> sentence.value.matchInfix(_last) { lhs, last ->
					lhs.matchPrefix(_previous) { previous ->
						previous.onlyFieldOrNull?.let { previousField ->
							push(last).pushOrNull(previousField)
						}
					}
				}
				else -> null
			}
		}
	}

val Field.valueStackOrNull: Stack<Value>?
	get() =
		stack<Value>().pushOrNull(this)?.reverse

val Value.stackOrNull: Stack<Value>?
	get() =
		onlyFieldOrNull?.valueStackOrNull

