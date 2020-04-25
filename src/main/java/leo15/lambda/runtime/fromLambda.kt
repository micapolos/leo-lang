package leo15.lambda.runtime

import leo15.lambda.AbstractionTerm
import leo15.lambda.ApplicationTerm
import leo15.lambda.IndexTerm
import leo15.lambda.ValueTerm
import leo15.lambda.Term as LambdaTerm

fun <T> term(lambdaTerm: LambdaTerm): Term<T> = term(lambdaTerm, null)

@Suppress("UNCHECKED_CAST")
fun <T> term(lambdaTerm: LambdaTerm, applicationOrNull: Application<T>?): Term<T> =
	when (lambdaTerm) {
		is ValueTerm -> term(value(lambdaTerm.value as T), applicationOrNull)
		is AbstractionTerm -> term(lambda(term(lambdaTerm.body)), applicationOrNull)
		is ApplicationTerm -> term(lambdaTerm.lhs, application(lambdaTerm.rhs, applicationOrNull))
		is IndexTerm -> term(at(lambdaTerm.index), applicationOrNull)
	}

@Suppress("UNCHECKED_CAST")
fun <T> application(lambdaTerm: LambdaTerm, applicationOrNull: Application<T>?): Application<T> =
	if (lambdaTerm is ApplicationTerm) application(lambdaTerm, applicationOrNull)
	else application(term(lambdaTerm, null), null)

@Suppress("UNCHECKED_CAST")
fun <T> application(applicationTerm: ApplicationTerm, applicationOrNull: Application<T>?): Application<T> =
	application(term(applicationTerm.lhs, null), application(applicationTerm.rhs, applicationOrNull))
