package leo

import leo.base.*

data class Pattern(
	val patternTermStack: Stack<Term<Pattern>>) {
	override fun toString() = reflect.string
}

val Stack<Term<Pattern>>.pattern
	get() =
		Pattern(this)

fun oneOf(patternTerm: Term<Pattern>, vararg patternTerms: Term<Pattern>) =
	stack(patternTerm, *patternTerms).pattern

val Term<Value>.parsePattern: Pattern?
	get() =
		structureTermOrNull?.let { listTerm ->
			true.fold(listTerm.fieldStack) { field ->
				this && field.key == eitherWord
			}.let { isOneOf ->
				if (!isOneOf) null
				else structureTermOrNull
					?.fieldStack
					?.reverse
					?.foldTop { field -> field.value.parsePatternTerm.stack }
					?.andPop { stack, field -> stack.push(field.value.parsePatternTerm) }
					?.pattern
			}
		}

// === matching

fun Term<Value>.matches(pattern: Pattern): Boolean =
	pattern.patternTermStack.top { patternTerm -> matches(patternTerm) } != null

// === matching

fun Term<Value>.matches(patternTerm: Term<Pattern>): Boolean =
	when (patternTerm) {
		is Term.Meta -> matches(patternTerm)
		is Term.Identifier -> this is Term.Identifier && this.matches(patternTerm)
		is Term.Structure -> this is Term.Structure && this.matches(patternTerm)
	}

fun Term<Value>.matches(patternTerm: Term.Meta<Pattern>): Boolean =
	matches(patternTerm.value)

fun Term.Identifier<Value>.matches(patternTerm: Term.Identifier<Pattern>) =
	word == patternTerm.word

fun Term.Structure<Value>.matches(patternTerm: Term.Structure<Pattern>): Boolean =
	fieldStack.top.matches(patternTerm.fieldStack.top) &&
		if (fieldStack.pop == null) patternTerm.fieldStack.pop == null
		else patternTerm.fieldStack.pop != null && fieldStack.pop.term.matches(patternTerm.fieldStack.pop.term)

fun Field<Value>.matches(patternTerm: Field<Pattern>) =
	key == patternTerm.key && value.matches(patternTerm.value)

// === parsing

val Term<Value>.parsePatternTerm: Term<Pattern>
	get() =
		when (this) {
			is Term.Meta -> fail
			is Term.Identifier -> Term.Identifier(word)
			is Term.Structure -> parsePattern?.metaTerm ?: parseListPattern
		}

val Term.Structure<Value>.parseListPattern: Term<Pattern>
	get() =
		fieldStack
			.reverse
			.foldTop { it.parsePatternField.stack }
			.andPop { stack, field -> stack.push(field.parsePatternField) }
			.term

val Field<Value>.parsePatternField: Field<Pattern>
	get() =
		key fieldTo value.parsePatternTerm

// === reflect

val Pattern.reflect: Field<Value>
	get() =
		oneWord fieldTo term(
			ofWord fieldTo patternTermStack.map { patternTerm ->
				patternTerm.reflect { oneOf ->
					term(oneOf.reflect)
				}
			}.term)