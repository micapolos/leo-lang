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

// === matching

fun Term<Nothing>.matches(pattern: Pattern): Boolean =
	pattern.patternTermStack.top { oneOfTerm ->
		matches(oneOfTerm)
	} != null

// === matching

fun Term<Nothing>.matches(patternTerm: Term<Pattern>): Boolean =
	when (patternTerm) {
		is Term.Meta -> matches(patternTerm)
		is Term.Structure -> this is Term.Structure && this.matches(patternTerm)
	}

fun Term<Nothing>.matches(patternMetaTerm: Term.Meta<Pattern>): Boolean =
	matches(patternMetaTerm.value)

fun Term.Structure<Nothing>.matches(patternStructureTerm: Term.Structure<Pattern>): Boolean =
	topField.matches(patternStructureTerm.topField) &&
		if (lhsTermOrNull == null) patternStructureTerm.lhsTermOrNull == null
		else patternStructureTerm.lhsTermOrNull != null &&
			lhsTermOrNull.matches(patternStructureTerm.lhsTermOrNull)

fun Field<Nothing>.matches(patternField: Field<Pattern>) =
	word == patternField.word &&
		if (termOrNull == null) patternField.termOrNull == null
		else patternField.termOrNull != null && termOrNull.matches(patternField.termOrNull)

// === parsing

val Term<Nothing>.parsePattern: Pattern?
	get() =
		structureTermOrNull?.run {
			true.fold(fieldStream) { field ->
				this && field.word == eitherWord && field.termOrNull != null
			}.let { isOneOf ->
				if (!isOneOf) null
				else fieldStream
					.reverse
					.foldFirst { field -> field.termOrNull!!.parsePatternTerm.onlyStack }
					.foldNext { field -> push(field.termOrNull!!.parsePatternTerm) }
					.oneOf
			}
		}

val Term<Nothing>.parsePatternTerm: Term<Pattern>
	get() =
		when (this) {
			is Term.Meta -> fail
			is Term.Structure -> parsePattern?.metaTerm ?: parseStructurePatternTerm
		}

val Term.Structure<Nothing>.parseStructurePatternTerm: Term<Pattern>
	get() =
		fieldStream
			.reverse
			.foldFirst { field -> field.parsePatternField.onlyStack }
			.foldNext { field -> push(field.parsePatternField) }
			.term

val Field<Nothing>.parsePatternField: Field<Pattern>
	get() =
		word fieldTo termOrNull?.parsePatternTerm

// === reflect

val Pattern.reflect: Field<Nothing>
	get() =
		patternWord fieldTo patternTermStack.reflect { patternTerm ->
			patternTerm.reflect(Pattern::reflect)
		}