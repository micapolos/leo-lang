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

val Term<Pattern>.patternOrNull: Pattern?
	get() =
		valueOrNull

fun Term<Nothing>.matches(pattern: Pattern): Boolean =
	pattern.patternTermStack.top { oneOfTerm ->
		matches(oneOfTerm)
	} != null

fun Term<Nothing>.matches(patternTerm: Term<Pattern>): Boolean =
	patternTerm.patternOrNull?.let { pattern ->
		matches(pattern)
	} ?:
	when (patternTerm) {
		is MetaTerm -> false
		is WordTerm -> this is WordTerm && word.matches(patternTerm.word)
		is StructureTerm -> this is StructureTerm && this.matches(patternTerm)
	}

fun Word.matches(patternWord: Word): Boolean =
	this == patternWord

fun StructureTerm<Nothing>.matches(patternStructureTerm: StructureTerm<Pattern>): Boolean =
	structure.matches(patternStructureTerm.structure)

fun Structure<Nothing>.matches(patternStructure: Structure<Pattern>): Boolean =
	fieldStack.top.matches(patternStructure.fieldStack.top) &&
		if (fieldStack.pop == null) patternStructure.fieldStack.pop == null
		else patternStructure.fieldStack.pop != null &&
			fieldStack.pop.structureTerm.matches(patternStructure.fieldStack.pop.structureTerm)

fun Field<Nothing>.matches(patternField: Field<Pattern>) =
	key == patternField.key && value.matches(patternField.value)

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
						}.oneOf
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

val Pattern.reflect: Term<Nothing>
	get() =
		patternTermStream.reflect(eitherWord) {
			reflectMetaTerm(Pattern::reflect)
		}
