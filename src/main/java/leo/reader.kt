package leo

import leo.base.andPop
import leo.base.foldTop
import leo.base.reverse

data class Reader(
	val function: Function,
	val script: Script)

val leoReaderField =
	leoWord fieldTo term<Nothing>(readerWord)

val leoReaderScript =
	script(term(leoReaderField))

val emptyReader =
	Reader(identityFunction, leoReaderScript)

fun <R> Reader.read(initial: R, byte: Byte, fn: (R, Byte) -> R): Pair<R, Reader>? =
	script
		.push(readWord fieldTo term(byte.reflect))
		?.let { newScript ->
			function
				.invoke(newScript)
				.let { resultScript ->
					resultScript
						.term
						.structureTermOrNull
						?.let { resultTerm ->
							resultTerm
								.fieldStack
								.reverse
								.foldTop { topField ->
									if (topField != leoReaderField) null
									else initial to copy(script = leoReaderScript)
								}
								.andPop { foldedAndReaderOrNull, followingField ->
									foldedAndReaderOrNull?.let { (folded, reader) ->
										when {
											followingField.key != readWord -> null
											else -> followingField.value.match(byteWord) { byteValue ->
												byteWord.fieldTo(byteValue).parseByte?.let { byte ->
													fn(folded, byte) to reader.copy(script = leoReaderScript)
												}
											}
										}
									}
								}
						} ?: initial to copy(script = resultScript)
				}
		}

fun Reader.push(field: Field<Nothing>): Reader? =
	script.push(field)?.let { newScript ->
		copy(script = newScript)
	}

