package leo

import leo.base.*

sealed class Pattern

data class OneOfPattern(
	val patternTermStack: Stack<Term<Pattern>>) : Pattern() {
	override fun toString() = reflect.string
}

data class RecursionPattern(
	val recursion: Recursion) : Pattern() {
	override fun toString() = reflect.string
}

val Stack<Term<Pattern>>.oneOfPattern
	get() =
		OneOfPattern(this)

val Recursion.pattern: Pattern
	get() =
		RecursionPattern(this)

fun oneOfPattern(patternTerm: Term<Pattern>, vararg patternTerms: Term<Pattern>): Pattern =
	stack(patternTerm, *patternTerms).oneOfPattern

val OneOfPattern.patternTermStream: Stream<Term<Pattern>>
	get() =
		patternTermStack.reverse.stream

// === matching

val Term<Pattern>.patternOrNull: Pattern?
	get() =
		valueOrNull

fun Term<Nothing>.matches(patternTerm: Term<Pattern>): Boolean =
	matches(patternTerm, null)

fun Term<Nothing>.matches(pattern: Pattern): Boolean =
	matches(pattern, null)

fun Field<Nothing>.matches(patternField: Field<Pattern>) =
	matches(patternField, null)

fun Term<Nothing>.matches(pattern: Pattern, backTraceOrNull: BackTrace?): Boolean =
	when (pattern) {
		is OneOfPattern -> matches(pattern, backTraceOrNull)
		is RecursionPattern -> matches(pattern, backTraceOrNull)
	}

fun Term<Nothing>.matches(pattern: OneOfPattern, backTraceOrNull: BackTrace?): Boolean =
	pattern.patternTermStack.top { oneOfTerm ->
		matches(oneOfTerm, backTraceOrNull)
	} != null

fun Term<Nothing>.matches(pattern: RecursionPattern, backTraceOrNull: BackTrace?): Boolean =
	backTraceOrNull != null && pattern.recursion.apply(backTraceOrNull.back)?.let { newBackTraceOrNull ->
		matches(newBackTraceOrNull.patternTermStack.top, newBackTraceOrNull.back)
	} ?: false

fun Term<Nothing>.matches(patternTerm: Term<Pattern>, backTraceOrNull: BackTrace?): Boolean =
	patternTerm.patternOrNull?.let { pattern ->
		matches(pattern, backTraceOrNull.push(patternTerm))
	} ?:
	when (patternTerm) {
		is MetaTerm -> false
		is WordTerm -> this is WordTerm && word.matches(patternTerm.word)
		is StructureTerm -> this is StructureTerm && this.matches(patternTerm, backTraceOrNull)
	}

fun Word.matches(patternWord: Word): Boolean =
	this == patternWord

fun StructureTerm<Nothing>.matches(patternStructureTerm: StructureTerm<Pattern>, backTraceOrNull: BackTrace?): Boolean =
	structure.matches(patternStructureTerm.structure, backTraceOrNull.push(patternStructureTerm))

fun Structure<Nothing>.matches(patternStructure: Structure<Pattern>, backTraceOrNull: BackTrace?): Boolean =
	fieldStack.top.matches(patternStructure.fieldStack.top, backTraceOrNull) &&
		if (fieldStack.pop == null) patternStructure.fieldStack.pop == null
		else patternStructure.fieldStack.pop != null &&
			fieldStack.pop.structureTerm.matches(patternStructure.fieldStack.pop.structureTerm, backTraceOrNull)

fun Field<Nothing>.matches(patternField: Field<Pattern>, backTraceOrNull: BackTrace?) =
	key == patternField.key && value.matches(patternField.value, backTraceOrNull)

// === parsing

val Term<Nothing>.parsePattern: Pattern?
	get() =
		structureTermOrNull
			?.run {
				true.fold(fieldStream) { field ->
					this && field.key == eitherWord
				}.let { isOneOf ->
					if (!isOneOf) null
					else fieldStream.run {
						first.value.parsePatternTerm.onlyStack.fold(nextOrNull) { field ->
							push(field.value.parsePatternTerm)
						}.oneOfPattern
					}
				}
			}

val Term<Nothing>.parsePatternTerm: Term<Pattern>
	get() =
		when (this) {
			is MetaTerm -> fail
			is WordTerm -> word.term
			is StructureTerm -> parsePattern?.meta?.term ?: parseStructurePatternTerm
		}

val StructureTerm<Nothing>.parseStructurePatternTerm: Term<Pattern>
	get() =
		fieldStream.run {
			first.parsePatternField.structure.fold(nextOrNull) { field ->
				plus(field.parsePatternField)
			}.term
		}

val Field<Nothing>.parsePatternField: Field<Pattern>
	get() =
		key fieldTo value.parsePatternTerm

// === reflect

val Pattern.reflect: Field<Nothing>
	get() =
		patternWord fieldTo term(when (this) {
			is OneOfPattern -> oneWord fieldTo term(ofWord fieldTo patternTermStream.termReflect(Pattern::reflect))
			is RecursionPattern -> recursion.reflect
		})
