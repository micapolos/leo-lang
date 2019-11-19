package leo14.typed

import leo13.fold
import leo13.reverse
import leo14.*
import leo14.native.Native
import leo14.typed.compiler.Dictionary

fun Processor<Token>.process(type: Type, dictionary: Dictionary): Processor<Token> =
	fold(type.lineStack.reverse) { process(it, dictionary) }

fun Processor<Token>.process(line: Line, dictionary: Dictionary): Processor<Token> =
	when (line) {
		is NativeLine -> error("$this.process($line)")
		is FieldLine -> process(line.field, dictionary)
		is ChoiceLine -> process(line.choice, dictionary)
		is ArrowLine -> process(line.arrow, dictionary)
	}

fun Processor<Token>.process(field: Field, dictionary: Dictionary): Processor<Token> =
	process(token(begin(field.string))).process(field.rhs, dictionary).process(token(end))

fun Processor<Token>.process(choice: Choice, dictionary: Dictionary): Processor<Token> =
	this
		.process(token(begin(dictionary.choice)))
		.fold(choice.optionStack.reverse) { process(it, dictionary) }
		.process(token(end))

fun Processor<Token>.process(option: Option, dictionary: Dictionary): Processor<Token> =
	this
		.process(token(begin(option.string)))
		.process(option.rhs, dictionary)
		.process(token(end))

fun Processor<Token>.process(arrow: Arrow, dictionary: Dictionary): Processor<Token> =
	this
		.process(token(begin(dictionary.action)))
		.process(arrow.lhs, dictionary)
		.process(token(begin(dictionary.giving)))
		.process(arrow.rhs, dictionary)
		.process(token(end))
		.process(token(end))

fun Processor<Token>.process(action: Action<Native>, dictionary: Dictionary): Processor<Token> =
	this
		.process(token(begin(dictionary.action)))
		.process(action.param, dictionary)
		.process(token(begin(dictionary.giving)))
		.process(action.body.type, dictionary)
		.process(token(end))
		.process(token(end))
