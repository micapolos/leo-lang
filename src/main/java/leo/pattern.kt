package leo

import leo.base.*

data class Pattern(
	val patternTermStack: Stack<Term<Pattern>>) {
	override fun toString() = reflect.string
}

val Stack<Term<Pattern>>.oneOf
	get() =
		Pattern(this)

fun pattern(patternTerm: Term<Pattern>, vararg patternTerms: Term<Pattern>): Pattern =
	stack(patternTerm, *patternTerms).oneOf

val Pattern.patternTermStream: Stream<Term<Pattern>>
	get() =
		patternTermStack.reverse.stream

// === matching

fun Term<Nothing>.matches(pattern: Pattern): Boolean =
	pattern.patternTermStack.top { oneOfTerm ->
		matches(oneOfTerm)
	} != null

fun Term<Nothing>.matches(patternTerm: Term<Pattern>): Boolean =
	when (patternTerm) {
		is MetaTerm -> this.matches(patternTerm)
		is WordTerm -> this is WordTerm && this.matches(patternTerm)
		is FieldsTerm -> this is FieldsTerm && this.matches(patternTerm)
	}

fun Term<Nothing>.matches(patternMetaTerm: MetaTerm<Pattern>): Boolean =
	matches(patternMetaTerm.value)

fun WordTerm<Nothing>.matches(patternWordTerm: WordTerm<Pattern>): Boolean =
	word == patternWordTerm.word

fun FieldsTerm<Nothing>.matches(patternFieldsTerm: FieldsTerm<Pattern>): Boolean =
	fieldStack.top.matches(patternFieldsTerm.fieldStack.top) &&
		if (fieldStack.pop == null) patternFieldsTerm.fieldStack.pop == null
		else patternFieldsTerm.fieldStack.pop != null &&
			fieldStack.pop.fieldsTerm.matches(patternFieldsTerm.fieldStack.pop.fieldsTerm)

fun Field<Nothing>.matches(patternField: Field<Pattern>) =
	key == patternField.key && value.matches(patternField.value)

// === parsing

val Term<Nothing>.parsePattern: Pattern?
	get() =
		fieldsTermOrNull
			?.run {
				true.fold(fieldStream) { field ->
					this && field.key == eitherWord
				}.let { isOneOf ->
					if (!isOneOf) null
					else fieldStream.run {
						first.value.parsePatternTerm.onlyStack.fold(nextOrNull) { field ->
							push(field.value.parsePatternTerm)
						}.oneOf
					}
				}
			}


val Term<Nothing>.parsePatternTerm: Term<Pattern>
	get() =
		when (this) {
			is MetaTerm -> fail
			is WordTerm -> word.term
			is FieldsTerm -> parsePattern?.metaTerm ?: parseStructurePatternTerm
		}

val FieldsTerm<Nothing>.parseStructurePatternTerm: Term<Pattern>
	get() =
		fieldStream.run {
			first.parsePatternField.term.fold(nextOrNull) { field ->
				fieldsPush(field.parsePatternField)
			}
		}

val Field<Nothing>.parsePatternField: Field<Pattern>
	get() =
		key fieldTo value.parsePatternTerm

// === reflect

val Pattern.reflect: Field<Nothing>
	get() =
		patternWord fieldTo patternTermStream.reflect {
			reflect(Pattern::reflect)
		}