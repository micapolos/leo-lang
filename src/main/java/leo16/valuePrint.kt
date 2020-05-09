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
		_taking.sentenceTo(pattern.asValue)

val Choice.printSentence: Sentence
	get() =
		_choice.sentenceTo(caseFieldStack.map { printSentence.field }.value)

val Dictionary.printSentence: Sentence
	get() =
		_dictionary.sentenceTo(
			_definition(definitionStack.map { printField.value }.field))

val Definition.printField: Field
	get() =
		_definition(pattern.value.plus(body.printField))

val Body.printField: Field
	get() =
		when (this) {
			is ValueBody -> _is(value)
			is FunctionBody -> _gives(function.bodyValue)
			is NativeBody -> _gives(apply.nativeField)
			is RecurseBody -> _recurse(function.bodyValue)
		}

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
