package leo20.tokenizer

import leo14.Literal
import leo14.NumberLiteral
import leo14.StringLiteral
import leo14.bigDecimal
import leo20.Line
import leo20.Scope
import leo20.Value
import leo20.line
import leo20.plus
import leo20.resolve
import leo20.value

data class Evaluated(
	val scope: Scope,
	val value: Value
)

val Evaluated.begin get() = Evaluated(scope, value())

fun Evaluated.push(literal: Literal): Evaluated =
	when (literal) {
		is StringLiteral -> Evaluated(scope, scope.bindings.resolve(value.plus(line(literal.string))))
		is NumberLiteral -> Evaluated(scope, scope.bindings.resolve(value.plus(line(literal.number.bigDecimal.toDouble().bigDecimal))))
	}

fun Evaluated.plus(line: Line): Evaluated =
	copy(value = scope.bindings.resolve(value.plus(line)))
