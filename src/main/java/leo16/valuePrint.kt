package leo16

import leo.base.notNullIf
import leo13.Stack
import leo13.isEmpty
import leo13.map
import leo16.names.*
import java.math.BigDecimal

val Value.printed: Value
	get() =
		null
			?: textPrintOrNull
			?: numberPrintOrNull
			?: stackPrintOrNull
			?: defaultPrinted

val Value.defaultPrinted: Value
	get() =
		when (this) {
			EmptyValue -> emptyValue
			is LinkValue -> link.printed
			is NativeValue -> native.nativeString().onlyValue
			is FunctionValue -> function.printed.onlyValue
			is LazyValue -> lazy.printedSentence.onlyValue
			is FuncValue -> TODO()
		}

val ValueLink.printed: Value
	get() =
		previousValue.printed.plus(lastSentence.printedSentence)

val Sentence.printedSentence: Sentence
	get() =
		word.sentenceTo(rhsValue.printed)

val Function.printed: Sentence
	get() =
		_function.sentenceTo(patternValue.plus(_does(compiled.bodyValue)))

val Lazy.printedSentence: Sentence
	get() =
		_lazy.sentenceTo(compiled.bodyValue.printed)

val Dictionary.printSentence: Sentence
	get() =
		_dictionary.sentenceTo(
			_definition(definitionStack.map { printField.onlyValue }.field))

val Definition.printField: Sentence
	get() =
		asSentence

fun <T> Stack<T>.printField(fn: T.() -> Sentence): Sentence =
	map(fn).printField

val Stack<Sentence>.printField: Sentence
	get() =
		_list(value)

val Value.stackPrintOrNull: Value?
	get() =
		stackOrNull?.let { stack ->
			_list(
				if (stack.isEmpty) value(_empty())
				else stack.map { _item.invoke(this) }.value
			).onlyValue
		}

val Value.textPrintOrNull: Value?
	get() =
		matchPrefix(_text) { rhs ->
			rhs.matchNative { native ->
				notNullIf(native is String) {
					this
				}
			}
		}

val Value.numberPrintOrNull: Value?
	get() =
		matchPrefix(_number) { rhs ->
			rhs.matchNative { native ->
				notNullIf(native is BigDecimal) {
					this
				}
			}
		}
