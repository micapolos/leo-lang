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
		is AnyLine -> this
			.processFn(token(begin(dictionary.script)) of typeKind)
			.processFn(token(end) of typeKind)
	}

fun Processor<Syntax>.process(field: Field, dictionary: Dictionary): Processor<Syntax> =
	this
		.processFn(token(begin(field.string)) of typeKind)
		.process(field.rhs, dictionary)
		.processFn(token(end) of typeKind)

fun Processor<Syntax>.process(choice: Choice, dictionary: Dictionary): Processor<Syntax> =
	this
		.processFn(token(begin(dictionary.choice)) of typeKeywordKind)
		.fold(choice.optionStack.reverse) { process(it, dictionary) }
		.processFn(token(end) of typeKeywordKind)

fun Processor<Syntax>.process(option: Option, dictionary: Dictionary): Processor<Syntax> =
	this
		.processFn(token(begin(option.string)) of typeKind)
		.process(option.rhs, dictionary)
		.processFn(token(end) of typeKind)

fun Processor<Syntax>.process(arrow: Arrow, dictionary: Dictionary): Processor<Syntax> =
	this
		.processFn(token(begin(dictionary.action)) of typeKeywordKind)
		.process(arrow.lhs, dictionary)
		.processFn(token(begin(dictionary.giving)) of typeKeywordKind)
		.process(arrow.rhs, dictionary)
		.processFn(token(end) of typeKeywordKind)
		.processFn(token(end) of typeKeywordKind)

fun Processor<Syntax>.process(action: Action<Native>, dictionary: Dictionary): Processor<Syntax> =
	this
		.processFn(token(begin(dictionary.action)) of valueKeywordKind)
		.processFn(token(begin(dictionary.doing)) of valueKeywordKind)
		.process(action.param, dictionary)
		.processFn(token(end) of valueKeywordKind)
		.processFn(token(end) of valueKeywordKind)
