package leo

import leo.base.andPop
import leo.base.foldTop
import leo.base.reverse
import leo.base.string

data class Reader(
	val valueTerm: Term<Value>) {
	override fun toString() = reflect.string
}

fun <V> leoReaderField(): Field<V> =
	leoWord fieldTo term(readerWord)

fun <V> leoReaderScript(): Term<V> =
	term(leoReaderField())

val emptyReader =
	Reader(leoReaderScript())

fun <R> R.read(
	reader: Reader,
	byte: Byte,
	readerFn: (Term<Value>) -> Term<Value>,
	readFn: R.(Byte) -> R): Pair<R, Reader>? =
	reader.valueTerm.push(readWord fieldTo term(byte.reflect))
		?.let { newScript ->
			readerFn(newScript)
				.let { resultScript ->
					resultScript.structureTermOrNull?.let { resultTerm ->
						resultTerm.fieldStack.reverse
							.foldTop { topField ->
								if (topField != leoReaderField<Value>()) null
								else to(reader.copy(valueTerm = leoReaderScript()))
							}
							.andPop { foldedAndReaderOrNull, followingField ->
								foldedAndReaderOrNull?.let { (folded, reader) ->
									when {
										followingField.key != readWord -> null
										else -> followingField.value.match(byteWord) { byteValue ->
											byteWord.fieldTo(byteValue).parseByte?.let { byte ->
												readFn(folded, byte) to reader.copy(valueTerm = leoReaderScript())
											}
										}
									}
								}
							}
					} ?: to(reader.copy(valueTerm = resultScript))
				}
		}

fun Reader.push(field: Field<Nothing>): Reader? =
	valueTerm.push(field)?.let { newScript ->
		copy(valueTerm = newScript)
	}

// === reflect

val Reader.reflect: Field<Value>
	get() =
		readerWord fieldTo valueTerm