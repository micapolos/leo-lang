package leo14.typed

import leo13.fold
import leo13.reverse
import leo14.*
import leo14.native.Native
import leo14.syntax.*

fun Processor<Syntax>.process(type: Type, language: Language): Processor<Syntax> =
	fold(type.lineStack.reverse) { process(it, language) }

fun Processor<Syntax>.process(line: Line, language: Language): Processor<Syntax> =
	when (line) {
		is NativeLine -> map(Token::valueSyntax).process(script(line.native.name)).map(Syntax::token)
		is FieldLine -> process(line.field, language)
		is ChoiceLine -> process(line.choice, language)
		is ArrowLine -> process(line.arrow, language)
		is AnyLine -> this
			.processFn(token(begin(Keyword.SCRIPT stringIn language)) of typeKind)
			.processFn(token(end) of typeKind)
	}

fun Processor<Syntax>.process(field: Field, language: Language): Processor<Syntax> =
	this
		.processFn(token(begin(field.string)) of typeKind)
		.process(field.rhs, language)
		.processFn(token(end) of typeKind)

fun Processor<Syntax>.process(choice: Choice, language: Language): Processor<Syntax> =
	this
		.processFn(token(begin(Keyword.CHOICE stringIn language)) of typeKeywordKind)
		.fold(choice.optionStack.reverse) { process(it, language) }
		.processFn(token(end) of typeKeywordKind)

fun Processor<Syntax>.process(option: Option, language: Language): Processor<Syntax> =
	this
		.processFn(token(begin(option.string)) of typeKind)
		.process(option.rhs, language)
		.processFn(token(end) of typeKind)

fun Processor<Syntax>.process(arrow: Arrow, language: Language): Processor<Syntax> =
	this
		.processFn(token(begin(Keyword.FUNCTION stringIn language)) of typeKeywordKind)
		.process(arrow.lhs, language)
		.processFn(token(begin(Keyword.GIVING stringIn language)) of typeKeywordKind)
		.process(arrow.rhs, language)
		.processFn(token(end) of typeKeywordKind)
		.processFn(token(end) of typeKeywordKind)

fun Processor<Syntax>.process(function: Function<Native>, language: Language): Processor<Syntax> =
	this
		.processFn(token(begin(Keyword.FUNCTION stringIn language)) of valueKeywordKind)
		.processFn(token(begin(Keyword.DOING stringIn language)) of valueKeywordKind)
		.process(function.takes, language)
		.processFn(token(end) of valueKeywordKind)
		.processFn(token(end) of valueKeywordKind)
