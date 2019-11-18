package leo14.typed

import leo13.fold
import leo13.reverse
import leo14.*

fun Processor<Token>.process(type: Type): Processor<Token> =
	fold(type.lineStack.reverse) { process(it) }

fun Processor<Token>.process(line: Line): Processor<Token> =
	when (line) {
		is NativeLine -> TODO()
		is FieldLine -> process(line.field)
		is ChoiceLine -> TODO()
		is ArrowLine -> TODO()
	}

fun Processor<Token>.process(field: Field): Processor<Token> =
	process(token(begin(field.string))).process(field.rhs).process(token(end))