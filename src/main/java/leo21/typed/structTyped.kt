package leo21.typed

import leo.base.fold
import leo.base.notNullIf
import leo.base.notNullOrError
import leo13.Link
import leo13.isEmpty
import leo13.linkOrNull
import leo13.linkTo
import leo14.lambda.Term
import leo14.lambda.pair
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
		if (isEmpty) typed.valueTerm
		else valueTerm.plus(typed.valueTerm),
		struct.plus(typed.line))

fun structTyped(vararg lines: LineTyped): StructTyped =
	emptyStructTyped.fold(lines) { plus(it) }

val StructTyped.typed
	get() =
		Typed(valueTerm, type(struct))

fun StructTyped.line(name: String): LineTyped =
	lineOrNull(name).notNullOrError("no field")

fun StructTyped.lineOrNull(name: String): LineTyped? =
	linkOrNull?.let { link ->
		if (link.head.line.name == name) link.head
		else link.tail.lineOrNull(name)
	}

val StructTyped.onlyLineOrNull: LineTyped?
	get() =
		linkOrNull?.let { link ->
			notNullIf(link.tail.isEmpty) {
				link.head
			}
		}

val StructTyped.onlyLine: LineTyped
	get() =
		onlyLineOrNull.notNullOrError("no only field")

val StructTyped.isEmpty: Boolean
	get() =
		struct.lineStack.isEmpty

val StructTyped.linkOrNull: Link<StructTyped, LineTyped>?
	get() =
		struct.lineStack.linkOrNull?.let { link ->
			if (link.stack.isEmpty) emptyStructTyped linkTo LineTyped(valueTerm, link.value)
			else valueTerm.pair().let { (lhs, rhs) ->
				StructTyped(lhs, Struct(link.stack)) linkTo LineTyped(rhs, link.value)
			}
		}
