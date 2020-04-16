package leo15.type

import leo.base.runIf
import leo14.ScriptLine
import leo14.lineTo
import leo14.script
import leo15.lambda.Term
import leo15.lambda.eval
import leo15.lambda.script
import leo15.string

data class Expression(val term: Term, val isConstant: Boolean) {
	override fun toString() = reflectScriptLine.string
}

val Expression.reflectScriptLine: ScriptLine
	get() =
		"expression" lineTo script(
			(if (isConstant) "constant" else "dynamic") lineTo term.script)

fun Term.expression(isConstant: Boolean) = Expression(this, isConstant)
val Term.constantExpression get() = expression(isConstant = true)
val Term.dynamicExpression get() = expression(isConstant = false)

val emptyExpression = emptyTerm.constantExpression

fun add(
	lhs: Expression,
	lhsIsStatic: Boolean,
	rhs: Expression,
	rhsIsStatic: Boolean): Expression =
	add(lhs.term, lhsIsStatic, rhs.term, rhsIsStatic)
		.expression(isConstant = lhs.isConstant && rhs.isConstant)

fun Expression.pair(lhsIsStatic: Boolean, rhsIsStatic: Boolean): Pair<Expression, Expression> =
	(if (isConstant) term.decompileUnplus(lhsIsStatic, rhsIsStatic)
	else term.unplus(lhsIsStatic, rhsIsStatic)).let { termPair ->
		termPair.first.expression(isConstant) to termPair.second.expression(isConstant)
	}

fun Expression.decompilePair(lhsIsStatic: Boolean, rhsIsStatic: Boolean): Pair<Expression, Expression> =
	eval.pair(lhsIsStatic, rhsIsStatic)

val Expression.eval: Expression
	get() =
		runIf(!isConstant) { term.eval.constantExpression }

val Expression.asDynamic: Expression
	get() =
		term.dynamicExpression