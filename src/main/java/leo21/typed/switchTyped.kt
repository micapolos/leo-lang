package leo21.typed

import leo.base.notNullOrError
import leo13.Stack
import leo13.isEmpty
import leo13.linkOrNull
import leo13.reverse
import leo14.lambda.Term
import leo14.lambda.invoke
import leo14.lambda.value.Value
import leo21.type.Line
import leo21.type.Type
import leo21.type.name
import leo21.type.type

data class SwitchTyped(
	val valueTerm: Term<Value>,
	val remainingLineTypedStack: Stack<Line>,
	val typeOrNull: Type?
)

val ChoiceTyped.switchTyped: SwitchTyped
	get() =
		SwitchTyped(valueTermOrNull!!, choice.lineStack, null)

fun SwitchTyped.reversedCase(name: String, typed: ArrowTyped): SwitchTyped =
	remainingLineTypedStack.linkOrNull.notNullOrError("exhausted").let { link ->
		if (link.value.name != name) error("case mismatch")
		else if (type(link.value) != typed.arrow.lhs) error("arrow lhs illegal")
		else SwitchTyped(
			valueTerm.invoke(typed.valueTerm),
			link.stack,
			if (typeOrNull == null) typed.arrow.rhs
			else if (typeOrNull != typed.arrow.rhs) error("type mismatch")
			else typeOrNull)
	}

val SwitchTyped.typed: Typed
	get() =
		if (!remainingLineTypedStack.isEmpty) error("not exaustive")
		else Typed(valueTerm, typeOrNull.notNullOrError("impossible"))
