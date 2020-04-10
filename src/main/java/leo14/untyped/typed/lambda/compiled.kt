package leo14.untyped.typed.lambda

import leo.base.ifOrNull
import leo14.lambda2.*
import leo14.untyped.typed.*

data class Compiled(val type: Type, val term: Term)
data class CompiledLink(val lhs: Compiled, val rhs: CompiledLine)
data class CompiledLine(val typeLine: TypeLine, val term: Term)
data class CompiledField(val typeField: TypeField, val term: Term)

fun Type.compiled(term: Term) = Compiled(this, term)
infix fun Compiled.linkTo(line: CompiledLine) = CompiledLink(this, line)
infix fun TypeLine.compiled(term: Term) = CompiledLine(this, term)
infix fun TypeField.compiled(term: Term) = CompiledField(this, term)

fun Compiled.plus(line: CompiledLine): Compiled =
	type.plus(line.typeLine).compiled(add(term, type.isStatic, line.term, line.typeLine.isStatic))

fun add(lhs: Term, lhsIsStatic: Boolean, rhs: Term, rhsIsStatic: Boolean): Term =
	if (lhsIsStatic)
		if (rhsIsStatic) lhs
		else rhs
	else
		if (rhsIsStatic) lhs
		else pair(lhs)(rhs)

fun Compiled.matchEmpty(fn: () -> Compiled?): Compiled? =
	ifOrNull(type.isEmpty, fn)

fun Compiled.matchLink(fn: Compiled.(CompiledLine) -> Compiled?): Compiled? =
	type.linkOrNull?.let { typeLink ->
		if (typeLink.lhs.isStatic || typeLink.line.isStatic)
			typeLink.lhs.compiled(term).fn(typeLink.line.compiled(term))
		else
			typeLink.lhs.compiled(at(1)).fn(typeLink.line.compiled(at(0)))?.let { compiled ->
				compiled.type.compiled(fn(fn(fn(compiled.term))(at(0)(first))(at(0)(second)))(term))
			}
	}

fun CompiledLine.matchField(fn: (CompiledField) -> Compiled?): Compiled? =
	typeLine.fieldOrNull?.let { field ->
		fn(field.compiled(term))
	}

fun CompiledLine.match(name: String, fn: (Compiled) -> Compiled?): Compiled? =
	typeLine.fieldOrNull?.let { field ->
		ifOrNull(field.name == name) {
			fn(field.rhs.compiled(term))
		}
	}

fun CompiledField.match(name: String, fn: (Compiled) -> Compiled?): Compiled? =
	ifOrNull(typeField.name == name) {
		fn(typeField.rhs.compiled(term))
	}

fun Compiled.matchInfix(name: String, fn: Compiled.(Compiled) -> Compiled?): Compiled? =
	matchLink { line ->
		line.match(name) { rhs ->
			fn(rhs)
		}
	}

fun Compiled.matchPrefix(name: String, fn: Compiled.() -> Compiled?): Compiled? =
	matchInfix(name) { rhs ->
		matchEmpty {
			rhs.fn()
		}
	}

fun Compiled.matchName(fn: String.() -> Compiled?): Compiled? =
	matchLink { line ->
		matchEmpty {
			line.typeLine.fieldOrNull?.let { field ->
				ifOrNull(field.rhs.isEmpty) {
					field.name.fn()
				}
			}
		}
	}

fun Compiled.matchNative(fn: Term.() -> Compiled?): Compiled? =
	matchLink { line ->
		matchEmpty {
			ifOrNull(line.typeLine is NativeTypeLine) {
				line.term.fn()
			}
		}
	}

fun Compiled.matchText(fn: Term.() -> Compiled?): Compiled? =
	ifOrNull(type == textType) {
		term.fn()
	}

val Compiled.eval: Compiled
	get() =
		type.compiled(term.eval)