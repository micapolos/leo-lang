package leo14.lib

import leo14.lambda.id
import leo14.typed.lineTo
import leo14.typed.type

val zeroTypeLine = "zero" lineTo type()

class Zero(term: Term) : Obj(term) {
	override val typeLine get() = zeroTypeLine
}

val zero = Zero(id())
