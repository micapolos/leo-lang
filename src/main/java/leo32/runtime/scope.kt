package leo32.runtime

import leo.base.Empty
import leo.base.fold

data class Scope(
	val types: Types,
	val templates: Templates)

val Empty.scope get() =
	Scope(types, templates)

fun Scope.defineType(term: Term, type: Term): Scope = TODO()
//	copy(types = types.put(term, type))

fun Scope.defineTemplate(type: Term, template: Template): Scope = TODO()
//	copy(templates = templates.put(type, template))

fun Scope.plus(field: TermField): Scope = TODO()
//	copy(types = types.plus(field))

fun Scope.plus(term: Term): Scope =
	fold(term.fieldSeq) { plus(it) }

fun Scope.invoke(term: Term): Term = TODO()
//	term.evalMacros.let { macrosTerm ->
//		types.at(macrosTerm.typeTerm)
//			?.invoke(parameter(term.evalMacros))
//			?:macrosTerm
//	}

fun Scope.type(term: Term): Term = TODO()
	//types.typeTerm(term)
