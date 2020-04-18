package leo15.core

import leo14.script
import leo14.scriptLine
import leo15.lambda.Term
import leo15.nothingName
import leo15.type.emptyTerm

val nothingTyp: Typ<Nothing> = Typ(nothingName.scriptLine) { Nothing }

object Nothing : Leo<Nothing>() {
	override val term: Term = emptyTerm
	override val typ: Typ<Nothing> = nothingTyp
	override val unsafeScript get() = script(nothingName)
}

val nothing = Nothing
