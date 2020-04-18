package leo15.core

import leo14.ScriptLine
import leo14.invoke
import leo15.*
import leo15.lambda.*

infix fun <F : Leo<F>, S : Leo<S>> Typ<F>.pairTypTo(second: Typ<S>) =
	Typ(pairName(firstName(scriptLine), secondName(second.scriptLine))) {
		Pair(this@pairTypTo, second, this)
	}

data class Pair<F : Leo<F>, S : Leo<S>>(
	val firstTyp: Typ<F>,
	val secondTyp: Typ<S>,
	override val term: Term) : Leo<Pair<F, S>>() {
	override val typ: Typ<Pair<F, S>> get() = firstTyp.pairTypTo(secondTyp)
	override val scriptLine: ScriptLine
		get() =
			pairName(firstName(first.scriptLine), secondName(second.scriptLine))
	val first: F get() = term.invoke(firstTerm).eval.leo(firstTyp)
	val second: S get() = term.invoke(secondTerm).eval.leo(secondTyp)
}

infix fun <F : Leo<F>, S : Leo<S>> F.pairTo(second: S): Pair<F, S> =
	pairTerm.invoke(term).invoke(second.term).leo(typ pairTypTo second.typ)