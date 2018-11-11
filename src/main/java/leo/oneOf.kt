package leo

import leo.base.*

data class OneOf(
    val patternStack: Stack<Pattern>) {
  override fun toString() = reflect.string
}

val Stack<Pattern>.oneOf
  get() =
    OneOf(this)

fun oneOf(pattern: Pattern, vararg patterns: Pattern) =
    stack(pattern, *patterns).oneOf

val Script.parseOneOf: OneOf?
  get() =
    term.structureTermOrNull?.let { listTerm ->
      listTerm.fieldStack.fold(true) { isOneOf, field ->
        isOneOf && field.key == eitherWord
      }.let { isOneOf ->
        if (!isOneOf) null
        else term
            .structureTermOrNull
            ?.fieldStack
            ?.reverse
            ?.foldTop { it.value.script.parsePattern.stack }
            ?.andPop { stack, field -> stack.push(field.value.script.parsePattern) }
            ?.oneOf
      }
    }

// === matching

fun Script.matches(oneOf: OneOf): Boolean =
    term.matches(oneOf)

fun Term<Nothing>.matches(oneOf: OneOf): Boolean =
    oneOf.patternStack.top { pattern -> matches(pattern.term) } != null

// === reflect

val OneOf.reflect: Field<Nothing>
  get() =
    oneWord fieldTo term(
        ofWord fieldTo patternStack
	        .reflect(Pattern::reflect))