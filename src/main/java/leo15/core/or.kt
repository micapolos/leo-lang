package leo15.core

import leo14.ScriptLine
import leo14.invoke
import leo15.Leo
import leo15.Typ
import leo15.eitherName
import leo15.lambda.*
import leo15.leo

infix fun <F : Leo<F>, S : Leo<S>> Typ<F>.or(secondTyp: Typ<S>): Typ<Or<F, S>> =
	Typ(eitherName(scriptLine, secondTyp.scriptLine)) {
		Or(this@or, secondTyp, this)
	}

data class Or<F : Leo<F>, S : Leo<S>>(
	val firstTyp: Typ<F>,
	val secondTyp: Typ<S>,
	override val term: Term) : Leo<Or<F, S>>() {
	override val typ get() = firstTyp or secondTyp
	override val scriptLine: ScriptLine
		get() =
			match(ScriptLine::class.java.javaTyp, { scriptLine.anyJava }, { scriptLine.anyJava }).value

	fun <R : Leo<R>> match(firstLambda: Lambda<F, R>, secondLambda: Lambda<S, R>): R =
		term
			.invoke(firstLambda.term)
			.invoke(secondLambda.term)
			.eval
			.leo(firstLambda.toTyp)

	fun <R : Leo<R>> match(typ: Typ<R>, firstFn: F.() -> R, secondFn: S.() -> R): R =
		term
			.invoke(fn { it.leo(firstTyp).firstFn().term })
			.invoke(fn { it.leo(secondTyp).secondFn().term })
			.eval
			.leo(typ)

}

infix fun <F : Leo<F>, S : Leo<S>> F.or(secondTyp: Typ<S>): Or<F, S> =
	Or(typ, secondTyp, choiceTerm(2, 1, term))

infix fun <F : Leo<F>, S : Leo<S>> Typ<F>.or(second: S): Or<F, S> =
	Or(this, second.typ, choiceTerm(2, 0, second.term))
