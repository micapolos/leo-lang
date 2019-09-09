package leo13

data class ExpressionApply(val expression: Expression)

fun ExpressionApply.atom(bindings: AtomBindings, functionAtom: Atom): Atom =
	expression.atom(bindings).let { parameterAtom ->
		functionAtom.function.expression.atom(bindings.plus(parameterAtom))
	}
