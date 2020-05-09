package leo16

import leo.base.notNullIf
import leo13.Stack
import leo13.map
import leo15.choiceName
import leo15.definitionName
import leo15.dictionaryName
import leo15.givesName
import leo15.isName
import leo15.listName
import leo15.takingName
import leo16.names.*
import java.math.BigDecimal

val Value.printed: Value
	get() =
		fieldStack.map { printed }.value

val Field.printed: Field
	get() =
		null
			?: textPrintOrNull
			?: numberPrintOrNull
			?: stackPrintOrNull
			?: defaultPrinted

val Field.defaultPrinted: Field
	get() =
		when (this) {
			is SentenceField -> sentence.printSentence.field
			is TakingField -> taking.printSentence.field
			is DictionaryField -> dictionary.printSentence.field
			is NativeField -> native.nativeString()
			is ChoiceField -> choice.printSentence.field
		}

val Sentence.printSentence: Sentence
	get() =
		word.sentenceTo(value.printed)

val Taking.printSentence: Sentence
	get() =
		takingName.sentenceTo(pattern.asValue)

val Choice.printSentence: Sentence
	get() =
		choiceName.sentenceTo(caseFieldStack.map { printSentence.field }.value)

val Dictionary.printSentence: Sentence
	get() =
		dictionaryName.sentenceTo(
			definitionName(definitionStack.map { printField.value }.field))

val Definition.printField: Field
	get() =
		definitionName(pattern.value.plus(body.printField))

val Body.printField: Field
	get() =
		when (this) {
			is ValueBody -> isName(value)
			is FunctionBody -> givesName(function.bodyValue)
			is NativeBody -> givesName(apply.nativeField)
		}

fun <T> Stack<T>.printField(fn: T.() -> Field): Field =
	map(fn).printField

val Stack<Field>.printField: Field
	get() =
		listName(value)

val Field.stackPrintValueOrNull: Value?
	get() =
		matchPrefix(_list) { rhs ->
			rhs.onlyFieldOrNull?.sentenceOrNull?.let { sentence ->
				when (sentence.word) {
					_empty -> value()
					_link -> sentence.value.matchInfix(_last) { lhs, last ->
						lhs.matchPrefix(_previous) { previous ->
							previous.onlyFieldOrNull?.stackPrintValueOrNull?.plus(_item(last.printed))
						}
					}
					else -> null
				}
			}
		}

val Field.stackPrintOrNull: Field?
	get() =
		stackPrintValueOrNull?.let { value ->
			_list(if (value.isEmpty) value(_empty()) else value)
		}

val Field.textPrintOrNull: Field?
	get() =
		matchPrefix(_text) { rhs ->
			rhs.matchNative { native ->
				notNullIf(native is String) {
					this
				}
			}
		}

val Field.numberPrintOrNull: Field?
	get() =
		matchPrefix(_number) { rhs ->
			rhs.matchNative { native ->
				notNullIf(native is BigDecimal) {
					this
				}
			}
		}
