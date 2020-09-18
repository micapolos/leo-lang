package leo16.term

data class Evaluator<V>(val nativeEval: V.() -> Thunk<V>)

fun <V> Evaluator<V>.eval(thunk: Thunk<V>): Thunk<V> =
	when (thunk.term) {
		is ValueTerm -> thunk.term.value.nativeEval()
		is VariableTerm -> thunk.scope[thunk.term.index]
		is AbstractionTerm -> thunk.scope.thunk(thunk.term.bodyTerm)
		is ApplicationTerm ->
			eval(thunk.scope.thunk(thunk.term.lhsTerm)).let { lhsEval ->
				eval(thunk.scope.thunk(thunk.term.rhsTerm)).let { rhsEval ->
					eval(thunk.scope.plus(rhsEval).thunk((lhsEval.term as AbstractionTerm).bodyTerm))
				}
			}
	}
