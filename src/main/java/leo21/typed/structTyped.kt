package leo21.typed

import leo.base.fold
import leo.base.notNullOrError
import leo13.EmptyStack
import leo13.LinkStack
import leo13.onlyOrNull
import leo14.lambda.Term
import leo14.lambda.first
import leo14.lambda.second
import leo14.lambda.value.Value
import leo21.term.nilTerm
import leo21.term.plus
import leo21.type.Struct
import leo21.type.name
import leo21.type.plus
import leo21.type.struct
import leo21.type.type

data class StructTyped(
	val valueTerm: Term<Value>,
	val struct: Struct
)

val emptyStructTyped = StructTyped(nilTerm, struct())

fun StructTyped.plus(typed: LineTyped): StructTyped =
	StructTyped(
		valueTerm.plus(typed.valueTerm),
		struct.plus(typed.line))

fun structTyped(vararg lines: LineTyped): StructTyped =
	emptyStructTyped.fold(lines) { plus(it) }

val StructTyped.typed
	get() =
		Typed(valueTerm, type(struct))

fun StructTyped.line(name: String): LineTyped =
	lineOrNull(name).notNullOrError("no field")

fun StructTyped.lineOrNull(name: String): LineTyped? =
	when (struct.lineStack) {
		is EmptyStack -> null
		is LinkStack -> struct.lineStack.link.let { link ->
			if (link.value.name == name) LineTyped(valueTerm.second, link.value)
			else StructTyped(valueTerm.first, Struct(link.stack)).lineOrNull(name)
		}
	}

val StructTyped.onlyLineOrNull: LineTyped?
	get() =
		struct.lineStack.onlyOrNull?.let { LineTyped(valueTerm.second, it) }

val StructTyped.onlyLine: LineTyped
	get() =
		onlyLineOrNull.notNullOrError("no only field")
