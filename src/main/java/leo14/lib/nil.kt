package leo14.lib

import leo14.lambda.id
import leo14.typed.lineTo
import leo14.typed.type

val nilTypeLine = "nil" lineTo type()

class Nil(term: Term) : Obj(term) {
	override val typeLine get() = nilTypeLine
}

val nil = Nil(id())
