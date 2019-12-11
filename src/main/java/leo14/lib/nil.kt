package leo14.lib

import leo14.lambda.id
import leo14.typed.lineTo
import leo14.typed.type

val nilTypeLine = "nil" lineTo type()

data class Nil(override val term: Term) : Obj() {
	override val typeLine get() = nilTypeLine
}

val nil = Nil(id())
