package leo14.typed

import leo13.fold
import leo13.reverse
import leo14.*
import leo14.native.Native
import leo14.syntax.*
import leo14.typed.compiler.Dictionary

fun Processor<Syntax>.process(type: Type, dictionary: Dictionary): Processor<Syntax> =
	fold(type.lineStack.reverse) { process(it, dictionary) }

fun Processor<Syntax>.process(line: Line, dictionary: Dictionary): Processor<Syntax> =
	when (line) {
		is NativeLine -> map(Token::valueSyntax).process(script(dictionary.native)).map(Syntax::token)
		is FieldLine -> process(line.field, dictionary)
		is ChoiceLine -> process(line.choice, dictionary)
		is ArrowLine -> process(line.arrow, dictionary)
	}

fun Processor<Syntax>.process(field: Field, dictionary: Dictionary): Processor<Syntax> =
	this
		.process(token(begin(field.string)) of typeKind)
		.process(field.rhs, dictionary)
		.process(token(end) of typeKind)

fun Processor<Syntax>.process(choice: Choice, dictionary: Dictionary): Processor<Syntax> =
	this
		.process(token(begin(dictionary.choice)) of typeKeywordKind)
		.fold(choice.optionStack.reverse) { process(it, dictionary) }
		.process(token(end) of typeKeywordKind)

fun Processor<Syntax>.process(option: Option, dictionary: Dictionary): Processor<Syntax> =
	this
		.process(token(begin(option.string)) of typeKind)
		.process(option.rhs, dictionary)
		.process(token(end) of typeKind)

fun Processor<Syntax>.process(arrow: Arrow, dictionary: Dictionary): Processor<Syntax> =
	this
		.process(token(begin(dictionary.action)) of typeKeywordKind)
		.process(arrow.lhs, dictionary)
		.process(token(begin(dictionary.giving)) of typeKeywordKind)
		.process(arrow.rhs, dictionary)
		.process(token(end) of typeKeywordKind)
		.process(token(end) of typeKeywordKind)

fun Processor<Syntax>.process(action: Action<Native>, dictionary: Dictionary): Processor<Syntax> =
	this
		.process(token(begin(dictionary.action)) of valueKeywordKind)
		.process(token(begin(dictionary.doing)) of valueKeywordKind)
		.process(action.param, dictionary)
		.process(token(end) of valueKeywordKind)
		.process(token(end) of valueKeywordKind)
