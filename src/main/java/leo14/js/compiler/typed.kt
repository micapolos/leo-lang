package leo13.js.compiler

import leo14.Script
import leo14.lambda.code.code
import leo14.lambda.js.Term
import leo14.lambda.js.term
import leo14.lambda.pair
import leo14.write

data class Typed(val term: Term, val type: Type)

infix fun Term.of(type: Type) = Typed(this, type)
val emptyTyped = term(code("null")) of emptyType

fun Typed.plus(string: String, rhs: Typed): Typed =
	pair(term, rhs.term) of type.plus(string fieldTo rhs.type)

val Script.typed
	get() =
		let { script ->
			compile {
				write(script)
			}
		}