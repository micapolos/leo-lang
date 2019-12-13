package leo14.lib

import leo14.lambda.id
import leo14.typed.lineTo
import leo14.typed.type

val oneTypeLine = "one" lineTo type()

class One(term: Term) : Obj(term) {
	override val typeLine get() = oneTypeLine
}

val one = One(id())
