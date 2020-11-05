package leo21.compiled

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

data class StructCompiled(
	val term: Term<Prim>,
	val struct: Struct
)

val emptyStructTyped = StructCompiled(nilTerm, struct())

fun StructCompiled.plus(compiled: LineCompiled): StructCompiled =
	StructCompiled(
		if (struct.isStatic)
			if (compiled.line.isStatic) nilTerm
			else compiled.term
		else
			if (compiled.line.isStatic) term
			else term.plus(compiled.term),
		struct.plus(compiled.line))

fun structTyped(vararg lines: LineCompiled): StructCompiled =
	emptyStructTyped.fold(lines) { plus(it) }

val StructCompiled.typed
	get() =
		Compiled(term, type(struct))

fun StructCompiled.line(name: String): LineCompiled =
	lineOrNull(name).notNullOrError("no field")

fun StructCompiled.lineOrNull(name: String): LineCompiled? =
	linkOrNull?.let { link ->
		if (link.head.line.name == name) link.head
		else link.tail.lineOrNull(name)
	}

val StructCompiled.onlyLineOrNull: LineCompiled?
	get() =
		linkOrNull?.let { link ->
			notNullIf(link.tail.isEmpty) {
				link.head
			}
		}

val StructCompiled.onlyLine: LineCompiled
	get() =
		onlyLineOrNull.notNullOrError("no only field")

val StructCompiled.isEmpty: Boolean
	get() =
		struct.lineStack.isEmpty

val StructCompiled.linkOrNull: Link<StructCompiled, LineCompiled>?
	get() =
		struct.lineStack.linkOrNull?.let { link ->
			if (Struct(link.stack).isStatic)
				if (link.value.isStatic) StructCompiled(nilTerm, Struct(link.stack)) linkTo LineCompiled(nilTerm, link.value)
				else StructCompiled(nilTerm, Struct(link.stack)) linkTo LineCompiled(term, link.value)
			else
				if (link.value.isStatic) StructCompiled(term, Struct(link.stack)) linkTo LineCompiled(nilTerm, link.value)
				else StructCompiled(term.first, Struct(link.stack)) linkTo LineCompiled(term.second, link.value)
		}

val StructCompiled.link: Link<StructCompiled, LineCompiled>
	get() =
		linkOrNull!!

val Compiled.link: Link<Compiled, LineCompiled>
	get() =
		struct.link.run { (tail.term of type(tail.struct)) linkTo head }

val Compiled.linkOrNull: Link<Compiled, LineCompiled>?
	get() =
		structOrNull?.linkOrNull?.run { (tail.term of type(tail.struct)) linkTo head }
