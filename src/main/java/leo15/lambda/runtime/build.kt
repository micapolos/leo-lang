package leo15.lambda.runtime

import leo15.lambda.builder.AbstractionTerm
import leo15.lambda.builder.ApplicationTerm
import leo15.lambda.builder.IndexTerm
import leo15.lambda.builder.ValueTerm
import leo15.lambda.builder.Term as BuilderTerm

val <T> BuilderTerm<T>.build get() = term(this)

fun <T> term(lambdaTerm: BuilderTerm<T>): Term<T> = term(lambdaTerm, null)

@Suppress("UNCHECKED_CAST")
fun <T> term(lambdaTerm: BuilderTerm<T>, applicationOrNull: Application<T>?): Term<T> =
	when (lambdaTerm) {
		is ValueTerm -> term(value(lambdaTerm.value), applicationOrNull)
		is AbstractionTerm -> term(lambda(term(lambdaTerm.body)), applicationOrNull)
		is ApplicationTerm -> term(lambdaTerm.lhs, application(lambdaTerm.rhs, applicationOrNull))
		is IndexTerm -> term(at(lambdaTerm.index), applicationOrNull)
	}

@Suppress("UNCHECKED_CAST")
fun <T> application(lambdaTerm: BuilderTerm<T>, applicationOrNull: Application<T>?): Application<T> =
	if (lambdaTerm is ApplicationTerm) application(lambdaTerm, applicationOrNull)
	else application(term(lambdaTerm, null), applicationOrNull)

@Suppress("UNCHECKED_CAST")
fun <T> application(applicationTerm: ApplicationTerm<T>, applicationOrNull: Application<T>?): Application<T> =
	application(term(applicationTerm.lhs, null), application(applicationTerm.rhs, applicationOrNull))
