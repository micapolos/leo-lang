package leo15.core

import leo14.invoke
import leo15.eitherName
import leo15.lambda.Term
import leo15.lambda.choiceTerm
import leo15.lambda.invoke
import leo15.lambda.unsafeUnchoice

infix fun <F : Leo<F>, S : Leo<S>> Typ<F>.or(secondTyp: Typ<S>): Typ<Or<F, S>> =
	Typ(eitherName(scriptLine, secondTyp.scriptLine)) {
		Or(this@or, secondTyp, this)
	}

data class Or<F : Leo<F>, S : Leo<S>>(
	val firstTyp: Typ<F>,
	val secondTyp: Typ<S>,
	override val term: Term) : Leo<Or<F, S>>() {
	override val typ: Typ<Or<F, S>> get() = firstTyp or secondTyp

	fun <R : Leo<R>> switch(forFirst: Lambda<F, R>, forSecond: Lambda<S, R>): R =
		term.invoke(forFirst.term).invoke(forSecond.term) of forFirst.toTyp

	val unsafeFirstOrNull: F?
		get() =
			term.unsafeUnchoice(2).run { if (index == 1) value.of(firstTyp) else null }
	val unsafeSecondOrNull: S?
		get() =
			term.unsafeUnchoice(2).run { if (index == 0) value.of(secondTyp) else null }
}

infix fun <F : Leo<F>, S : Leo<S>> F.or(secondTyp: Typ<S>): Or<F, S> =
	Or(typ, secondTyp, choiceTerm(2, 1, term))

infix fun <F : Leo<F>, S : Leo<S>> Typ<F>.or(second: S): Or<F, S> =
	Or(this, second.typ, choiceTerm(2, 0, second.term))
