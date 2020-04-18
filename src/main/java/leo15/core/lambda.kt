package leo15.core

import leo14.ScriptLine
import leo14.invoke
import leo15.*
import leo15.lambda.Term
import leo15.lambda.eval
import leo15.lambda.invoke

infix fun <F : Leo<F>, S : Leo<S>> Typ<F>.lambdaTypTo(toTyp: Typ<S>): Typ<Lambda<F, S>> =
	Typ(lambdaName(fromName(scriptLine), toName(toTyp.scriptLine))) {
		Lambda(this@lambdaTypTo, toTyp, this)
	}

data class Lambda<F : Leo<F>, S : Leo<S>>(
	val fromTyp: Typ<F>,
	val toTyp: Typ<S>,
	override val term: Term) : Leo<Lambda<F, S>>() {
	override val typ get() = fromTyp lambdaTypTo toTyp
	override val scriptLine: ScriptLine get() = typ.scriptLine
	operator fun invoke(first: F): S = term.invoke(first.term).eval.leo(toTyp)
}

fun <F : Leo<F>, S : Leo<S>> Typ<F>.gives(secondTyp: Typ<S>, fn: F.() -> S): Lambda<F, S> =
	Lambda(this, secondTyp, leo15.lambda.fn { it.leo(this).fn().term })
