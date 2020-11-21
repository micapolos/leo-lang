package leo21.compiled

import leo13.fold
import leo13.reverse
import leo14.lambda.Term
import leo14.lambda.arg
import leo14.lambda.fn
import leo14.lambda.invoke
import leo21.prim.Prim
import leo21.type.Choice
import leo21.type.Field
import leo21.type.fieldTo
import leo21.type.matches
import leo21.type.nameOrNull

data class FieldCompiled(val term: Term<Prim>, val field: Field)

infix fun Term<Prim>.of(field: Field) = FieldCompiled(this, field)

infix fun String.fieldTo(compiled: Compiled) =
	FieldCompiled(compiled.term, this fieldTo compiled.type)

infix fun Choice.compiled(compiled: LineCompiled): Compiled =
	choiceTyped {
		fold(lineStack.reverse) { case ->
			if (compiled.line.matches(case.nameOrNull!!)) plusChosen(LineCompiled(arg(0), compiled.line))
			else plusNotChosen(case)
		}
	}.run { fn(term).invoke(compiled.term).of(type) }

val FieldCompiled.rhsCompiled: Compiled get() = Compiled(term, field.rhs)
