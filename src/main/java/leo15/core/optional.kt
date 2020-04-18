package leo15.core

import leo14.invoke
import leo15.absentName
import leo15.lambda.Term
import leo15.optionalName
import leo15.presentName

val <T : Leo<T>> Typ<T>.optionalTyp: Typ<Optional<T>>
	get() =
		Typ(optionalName(scriptLine)) { Optional(this@optionalTyp, this) }

data class Optional<T : Leo<T>>(val itemTyp: Typ<T>, override val term: Term) : Leo<Optional<T>>() {
	override val typ get() = itemTyp.optionalTyp
	override val scriptLine
		get() = optionalName(
			orNull
				?.run { presentName(scriptLine) }
				?: absentName(itemTyp.scriptLine))
	private val nothingOrItem get() = Or(nothingTyp, itemTyp, term)
	val orNull: T? get() = nothingOrItem.secondOrNull
}

val <T : Leo<T>> Typ<T>.optional: Optional<T>
	get() =
		Optional(this, nothing.or(this).term)

val <T : Leo<T>> T.optional: Optional<T>
	get() =
		Optional(typ, nothingTyp.or(this).term)
