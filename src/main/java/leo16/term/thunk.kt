package leo16.term

data class Thunk<out V>(val scope: Scope<V>, val term: Term<V>)

fun <V> Scope<V>.thunk(term: Term<V>) = Thunk(this, term)

fun <V> Thunk<V>.eval(nativeEval: V.() -> Thunk<V>): Thunk<V> =
	when (term) {
		is ValueTerm -> term.value.nativeEval()
		is VariableTerm -> scope[term.index]
		is AbstractionTerm -> scope.thunk(term.bodyTerm)
		is ApplicationTerm ->
			scope.thunk(term.lhsTerm).eval(nativeEval).let { lhsEval ->
				scope.thunk(term.rhsTerm).eval(nativeEval).let { rhsEval ->
					scope.plus(rhsEval).thunk((lhsEval.term as AbstractionTerm).bodyTerm).eval(nativeEval)
				}
			}
	}
