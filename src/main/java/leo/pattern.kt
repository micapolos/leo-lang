package leo

import leo.base.*

sealed class Pattern {
	object Anything : Pattern() {
		override fun toString() = reflect.string
	}

	data class OneOf(
		val patternTermStack: Stack<Term<Pattern>>) : Pattern() {
		override fun toString() = reflect.string
	}
}

val Stack<Term<Pattern>>.oneOfPattern
	get() =
		Pattern.OneOf(this)

val anythingPattern =
	Pattern.Anything

fun oneOfPattern(patternTerm: Term<Pattern>, vararg patternTerms: Term<Pattern>) =
	stack(patternTerm, *patternTerms).oneOfPattern

val Term<*>.parsePattern: Pattern?
	get() =
		structureTermOrNull?.let { listTerm ->
			true.fold(listTerm.fieldStack.stream) { field ->
				this && field.key == eitherWord
			}.let { isOneOf ->
				if (!isOneOf) null
				else structureTermOrNull
					?.fieldStack
					?.reverse
					?.stream
					?.foldFirst { field -> field.value.parsePatternTerm.stack }
					?.foldNext { field -> push(field.value.parsePatternTerm) }
					?.oneOfPattern
			}
		}

// === matching

fun Term<Value>.matches(pattern: Pattern): Boolean =
	when (pattern) {
		is Pattern.Anything -> true
		is Pattern.OneOf -> matches(pattern)
	}

fun Term<Value>.matches(oneOfPattern: Pattern.OneOf): Boolean =
	oneOfPattern.patternTermStack.top { patternTerm ->
		matches(patternTerm)
	} != null

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

val Term<*>.parsePatternTerm: Term<Pattern>
	get() =
		when (this) {
			is Term.Meta -> fail
			is Term.Identifier -> parsePatternTerm
			is Term.Structure -> parsePattern?.metaTerm ?: parseListPattern
		}

val Term.Identifier<*>.parsePatternTerm: Term<Pattern>
	get() =
		if (word == anythingWord) Pattern.Anything.metaTerm
		else Term.Identifier(word)

val Term.Structure<*>.parseListPattern: Term<Pattern>
	get() =
		fieldStack
			.reverse
			.stream
			.foldFirst { field -> field.parsePatternField.stack }
			.foldNext { field -> push(field.parsePatternField) }
			.term

val Field<*>.parsePatternField: Field<Pattern>
	get() =
		key fieldTo value.parsePatternTerm

// === reflect

val Pattern.reflect: Field<Value>
	get() =
		patternWord fieldTo patternReflect

val Pattern.patternReflect: Term<Value>
	get() =
		when (this) {
			is Pattern.Anything -> term(anythingWord)
			is Pattern.OneOf -> this.patternReflect
		}

val Pattern.OneOf.patternReflect: Term<Value>
	get() =
		term(oneWord fieldTo term(
			ofWord fieldTo patternTermStack.map { patternTerm ->
				patternTerm.reflect { oneOf ->
					term(oneOf.reflect)
				}
			}.term))