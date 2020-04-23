package leo15.lambda.runtime

import leo15.lambda.AbstractionTerm
import leo15.lambda.ApplicationTerm
import leo15.lambda.IndexTerm
import leo15.lambda.ValueTerm
import leo15.lambda.Term as LambdaTerm

@Suppress("UNCHECKED_CAST")
fun <T> term(lambdaTerm: LambdaTerm): Term<T> =
	when (lambdaTerm) {
		is ValueTerm -> term(atom(lambdaTerm), null)
		is AbstractionTerm -> term(atom(term(lambdaTerm.body)), null)
		is ApplicationTerm -> TODO()//term(atom(lambdaTerm), term(lambdaTerm.rhs))
		is IndexTerm -> term(atom(lambdaTerm.index), null)
	}

@Suppress("UNCHECKED_CAST")
fun <T> atom(lambdaTerm: LambdaTerm): Atom<T> =
	when (lambdaTerm) {
		is ValueTerm -> atom(lambdaTerm.value as T)
		is AbstractionTerm -> atom(term(lambdaTerm.body))
		is ApplicationTerm -> null!!
		is IndexTerm -> atom(lambdaTerm.index)
	}