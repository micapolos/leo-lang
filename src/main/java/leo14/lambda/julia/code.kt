package leo14.lambda.julia

import leo14.lambda.AbstractionTerm
import leo14.lambda.ApplicationTerm
import leo14.lambda.NativeTerm
import leo14.lambda.Term
import leo14.lambda.VariableTerm
import leo14.lambda.code.Gen
import leo14.lambda.code.gen
import leo14.lambda.code.inc
import leo14.lambda.code.index
import leo14.lambda.code.paramCode
import leo14.lambda.code.varCode

val Term<Julia>.code: String get() = code(gen)

fun Term<Julia>.code(gen: Gen): String =
	when (this) {
		is NativeTerm -> native.string
		is AbstractionTerm -> "${paramCode(gen)}->${gen.inc { abstraction.body.code(it) }}"
		is ApplicationTerm -> "(${application.lhs.code(gen)})(${application.rhs.code(gen)})"
		is VariableTerm -> variable.index(gen).varCode
	}
