package leo15.core

import leo14.invoke
import leo15.lambda.Term
import leo15.optionalName

val <T : Leo<T>> Typ<T>.optionalTyp: Typ<Optional<T>>
	get() =
		Typ(optionalName(scriptLine)) { Optional(this@optionalTyp, this) }

data class Optional<T : Leo<T>>(val itemTyp: Typ<T>, override val term: Term) : Leo<Optional<T>>() {
	override val typ get() = itemTyp.optionalTyp
	private val nothingOrItem get() = Or(nothingTyp, itemTyp, term)
	fun <R : Leo<R>> switch(forAbsent: Lambda<Nothing, R>, forPresent: Lambda<T, R>): R =
		nothingOrItem.switch(forAbsent, forPresent)

	val unsafeOrNull: T? get() = nothingOrItem.unsafeSecondOrNull
}

val <T : Leo<T>> Typ<T>.absent: Optional<T>
	get() =
		Optional(this, nothing.or(this).term)

val <T : Leo<T>> T.present: Optional<T>
	get() =
		Optional(typ, nothingTyp.or(this).term)
