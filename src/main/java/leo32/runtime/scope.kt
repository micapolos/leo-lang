package leo32.runtime

import leo.base.Empty
import leo.base.fold

data class Scope(
	val typeTerms: TypeTerms,
	val templates: Templates)

val Empty.scope get() =
	Scope(typeTerms, templates)

fun Scope.defineType(term: Term, type: Term) =
	copy(typeTerms = typeTerms.put(term, type))

fun Scope.defineTemplate(type: Term, template: Template) =
	copy(templates = templates.put(type, template))

fun Scope.plus(field: TermField): Scope =
	copy(typeTerms = typeTerms.plus(field))

fun Scope.plus(term: Term): Scope =
	fold(term.fieldSeq) { plus(it) }

fun Scope.invoke(term: Term): Term =
	term.evalMacros.let { macrosTerm ->
		templates.at(macrosTerm.typeTerm)
			?.invoke(parameter(term.evalMacros))
			?:macrosTerm
	}

fun Scope.type(term: Term) =
	typeTerms.typeTerm(term)
