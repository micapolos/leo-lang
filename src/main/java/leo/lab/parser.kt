package leo.lab

import leo.*
import leo.base.*

val Field<Nothing>.parseBit: Bit?
	get() =
		when {
			this == bitWord fieldTo zeroWord.term -> Bit.ZERO
			this == bitWord fieldTo oneWord.term -> Bit.ONE
			else -> null
		}

val Field<Nothing>.parseByte: Byte?
	get() =
		if (word != byteWord) null
		else termOrNull
			?.parseStack { field -> field.parseBit }
			?.let { bitStack ->
				if (bitStack.sizeInt != 8) null
				else 0x00.fold(bitStack.reverse.stream) { bit ->
					shl(1).or(bit.int)
				}.toByte()
			}

fun <V> Term<Nothing>.parseStack(parseValue: (Field<Nothing>) -> V?): Stack<V>? =
	structureTermOrNull
		?.fieldStream
		?.reverse
		?.foldFirst { field ->
			parseValue(field)?.onlyStack
		}
		?.foldNext { field ->
			parseValue(field)?.let { value ->
				push(value)
			}
		}

val Field<Nothing>.parseLetter: Letter?
	get() =
		match(letterWord) { letterTerm ->
			letterTerm?.onlyFieldOrNull?.word?.letterStack?.theOnlyOrNull?.value
		}

val Field<Nothing>.parseCharacter: Character?
	get() =
		match(characterWord) { characterTerm ->
			when (characterTerm) {
				beginWord.term -> BeginCharacter
				endWord.term -> EndCharacter
				else -> letterWord.fieldTo(characterTerm).parseLetter?.let { letter ->
					LetterCharacter(letter)
				}
			}
		}
