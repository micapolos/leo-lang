package leo15.core

import leo14.invoke
import leo15.firstName
import leo15.lambda.*
import leo15.pairName
import leo15.secondName

infix fun <F : Leo<F>, S : Leo<S>> Typ<F>.and(second: Typ<S>) =
	Typ(pairName(firstName(scriptLine), secondName(second.scriptLine))) {
		And(this@and, second, this)
	}

data class And<F : Leo<F>, S : Leo<S>>(
	val firstTyp: Typ<F>,
	val secondTyp: Typ<S>,
	override val term: Term) : Leo<And<F, S>>() {
	override val typ: Typ<And<F, S>> get() = firstTyp.and(secondTyp)
	val first: F get() = term.invoke(firstTerm).of(firstTyp)
	val second: S get() = term.invoke(secondTerm).of(secondTyp)
	val unsafeFirst: F get() = term.unsafeUnpair.first of firstTyp
	val unsafeSecond: S get() = term.unsafeUnpair.second of secondTyp
}

infix fun <F : Leo<F>, S : Leo<S>> F.and(second: S): And<F, S> =
	pairTerm.invoke(term).invoke(second.term).of(typ and second.typ)