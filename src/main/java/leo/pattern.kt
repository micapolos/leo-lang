package leo

import leo.base.*

data class Pattern(
	val term: Term<OneOf>) {
	override fun toString() = reflect.string
}

val Term<OneOf>.pattern: Pattern
	get() =
		Pattern(this)

fun pattern(term: Term<OneOf>): Pattern =
	term.pattern

fun pattern(oneOf: OneOf): Pattern =
	oneOf.metaTerm.pattern

// === Script parsing ===

val Script.parsePattern: Pattern
	get() =
		when (term) {
			is Term.Meta -> fail
			is Term.Identifier -> Term.Identifier<OneOf>(term.word).pattern
			is Term.Structure -> parseOneOf?.metaTerm?.pattern ?: term.parseListPattern
		}

val Term.Structure<Nothing>.parseListPattern: Pattern
	get() =
		fieldStack
			.reverse
			.foldTop { it.parsePatternField.stack }
			.andPop { stack, field -> stack.push(field.parsePatternField) }
			.term
			.pattern

val Field<Nothing>.parsePatternField: Field<OneOf>
	get() =
		key fieldTo value.script.parsePattern.term

// === matching

fun Script.matches(pattern: Pattern): Boolean =
	term.matches(pattern.term)

fun Term<Nothing>.matches(oneOfTerm: Term<OneOf>): Boolean =
	when (oneOfTerm) {
		is Term.Meta -> matches(oneOfTerm)
		is Term.Identifier -> this is Term.Identifier && this.matches(oneOfTerm)
		is Term.Structure -> this is Term.Structure && this.matches(oneOfTerm)
	}

fun Term<Nothing>.matches(oneOfTerm: Term.Meta<OneOf>): Boolean =
	matches(oneOfTerm.value)

fun Term.Identifier<Nothing>.matches(oneOfTerm: Term.Identifier<OneOf>) =
	word == oneOfTerm.word

fun Term.Structure<Nothing>.matches(oneOfTerm: Term.Structure<OneOf>): Boolean =
	fieldStack.top.matches(oneOfTerm.fieldStack.top) &&
		if (fieldStack.pop == null) oneOfTerm.fieldStack.pop == null
		else oneOfTerm.fieldStack.pop != null && fieldStack.pop.term.matches(oneOfTerm.fieldStack.pop.term)

fun Field<Nothing>.matches(oneOfTerm: Field<OneOf>) =
	key == oneOfTerm.key && value.matches(oneOfTerm.value)

// === reflect

val Pattern.reflect: Field<Nothing>
	get() =
		patternWord fieldTo term(term.reflect { oneOf -> term(oneOf.reflect) })