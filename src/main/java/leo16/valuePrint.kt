package leo16

import leo.base.notNullIf
import leo13.Stack
import leo13.isEmpty
import leo13.map
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
			is FunctionField -> function.printSentence.field
			is DictionaryField -> dictionary.printSentence.field
			is NativeField -> native.nativeString()
			is ChoiceField -> choice.printSentence.field
			is LazyField -> lazy.printSentence.field
		}

val Sentence.printSentence: Sentence
	get() =
		word.sentenceTo(value.printed)

val Function.printSentence: Sentence
	get() =
		_function.sentenceTo(pattern.asValue.plus(_does(compiled.bodyValue)))

val Choice.printSentence: Sentence
	get() =
		_choice.sentenceTo(caseFieldStack.map { printSentence.field }.value)

val Lazy.printSentence: Sentence
	get() =
		_lazy.sentenceTo(compiled.bodyValue.printed)

val Dictionary.printSentence: Sentence
	get() =
		_dictionary.sentenceTo(
			_definition(definitionStack.map { printField.value }.field))

val Definition.printField: Field
	get() =
		asField

fun <T> Stack<T>.printField(fn: T.() -> Field): Field =
	map(fn).printField

val Stack<Field>.printField: Field
	get() =
		_list(value)

val Field.stackPrintOrNull: Field?
	get() =
		stackOrNull?.let { stack ->
			_list(if (stack.isEmpty) value(_empty()) else stack.map { _item(this) }.value)
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
