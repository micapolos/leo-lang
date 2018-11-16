package leo.lab

import leo.base.foldFirst
import leo.base.foldNext
import leo.base.reverse
import leo.base.string
import leo.byteWord
import leo.leoWord
import leo.readWord
import leo.readerWord

data class Reader(
	val valueTerm: Term<Nothing>) {
	override fun toString() = reflect.string
}

val leoReaderField: Field<Nothing> =
	leoWord fieldTo readerWord.term

val leoReaderTerm: Term<Nothing> =
	leoReaderField.term

val emptyReader =
	Reader(leoReaderTerm)

fun <R> R.read(
	reader: Reader,
	byte: Byte,
	readerFn: (Term<Nothing>) -> Term<Nothing>,
	readFn: R.(Byte) -> R): Pair<R, Reader>? =
	reader.valueTerm.push(readWord fieldTo term(byte.reflect)).let { newValueTerm ->
		readerFn(newValueTerm)
			.let { resultValueTerm ->
				resultValueTerm.structureTermOrNull?.let { resultTerm ->
					resultTerm.fieldStream.reverse
						.foldFirst { topField ->
							if (topField != leoReaderField) null
							else to(reader.copy(valueTerm = leoReaderTerm))
						}
						.foldNext { followingField ->
							this?.let { (folded, reader) ->
								when {
									followingField.word != readWord -> null
									else -> followingField.termOrNull?.match(byteWord) { byteValue ->
										byteWord.fieldTo(byteValue).parseByte?.let { byte ->
											readFn(folded, byte) to reader.copy(valueTerm = leoReaderTerm)
										}
									}
								}
							}
						}
				} ?: to(reader.copy(valueTerm = resultValueTerm))
			}
	}

fun Reader.push(field: Field<Nothing>): Reader =
	copy(valueTerm = valueTerm.push(field))

// === reflect

val Reader.reflect: Field<Nothing>
	get() =
		readerWord fieldTo valueTerm