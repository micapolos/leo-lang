package leo21.typed

import leo.base.fold
import leo.base.notNullIf
import leo.base.notNullOrError
import leo13.Link
import leo13.isEmpty
import leo13.linkOrNull
import leo13.linkTo
import leo14.lambda.Term
import leo14.lambda.first
import leo14.lambda.second
import leo21.prim.Prim
import leo21.term.nilTerm
import leo21.term.plus
import leo21.type.Struct
import leo21.type.isStatic
import leo21.type.name
import leo21.type.plus
import leo21.type.struct
import leo21.type.type

data class StructTyped(
	val term: Term<Prim>,
	val struct: Struct
)

val emptyStructTyped = StructTyped(nilTerm, struct())

fun StructTyped.plus(typed: LineTyped): StructTyped =
	StructTyped(
		if (struct.isStatic)
			if (typed.line.isStatic) nilTerm
			else typed.term
		else
			if (typed.line.isStatic) term
			else term.plus(typed.term),
		struct.plus(typed.line))

fun structTyped(vararg lines: LineTyped): StructTyped =
	emptyStructTyped.fold(lines) { plus(it) }

val StructTyped.typed
	get() =
		Typed(term, type(struct))

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
			if (Struct(link.stack).isStatic)
				if (link.value.isStatic) StructTyped(nilTerm, Struct(link.stack)) linkTo LineTyped(nilTerm, link.value)
				else StructTyped(nilTerm, Struct(link.stack)) linkTo LineTyped(term, link.value)
			else
				if (link.value.isStatic) StructTyped(term, Struct(link.stack)) linkTo LineTyped(nilTerm, link.value)
				else StructTyped(term.first, Struct(link.stack)) linkTo LineTyped(term.second, link.value)
		}

val StructTyped.link: Link<StructTyped, LineTyped>
	get() =
		linkOrNull!!

val Typed.link: Link<Typed, LineTyped>
	get() =
		struct.link.run { (tail.term of type(tail.struct)) linkTo head }

val Typed.linkOrNull: Link<Typed, LineTyped>?
	get() =
		structOrNull?.linkOrNull?.run { (tail.term of type(tail.struct)) linkTo head }
