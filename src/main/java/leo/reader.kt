package leo

import leo.base.*

data class Reader(
	val valueTerm: Term<Value>) {
	override fun toString() = reflect.string
}

fun <V> leoReaderField(): Field<V> =
	leoWord fieldTo term(readerWord)

fun <V> leoReaderTerm(): Term<V> =
	term(leoReaderField())

val emptyReader =
	Reader(leoReaderTerm())

fun <R> R.read(
	reader: Reader,
	byte: Byte,
	readerFn: (Term<Value>) -> Term<Value>,
	readFn: R.(Byte) -> R): Pair<R, Reader>? =
	reader.valueTerm.push(readWord fieldTo term(byte.reflect))
		?.let { newValueTerm ->
			readerFn(newValueTerm)
				.let { resultValueTerm ->
					resultValueTerm.structureTermOrNull?.let { resultTerm ->
						resultTerm.fieldStack.reverse.stream
							.foldFirst { topField ->
								if (topField != leoReaderField<Value>()) null
								else to(reader.copy(valueTerm = leoReaderTerm()))
							}
							.foldNext { followingField ->
								this?.let { (folded, reader) ->
									when {
										followingField.key != readWord -> null
										else -> followingField.value.match(byteWord) { byteValue ->
											byteWord.fieldTo(byteValue).parseByte?.let { byte ->
												readFn(folded, byte) to reader.copy(valueTerm = leoReaderTerm())
											}
										}
									}
								}
							}
					} ?: to(reader.copy(valueTerm = resultValueTerm))
				}
		}

fun Reader.push(field: Field<Value>): Reader? =
	valueTerm.push(field)?.let { newValueTerm ->
		copy(valueTerm = newValueTerm)
	}

// === reflect

val Reader.reflect: Field<Value>
	get() =
		readerWord fieldTo valueTerm