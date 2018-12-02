package leo.lab

import leo.Word

sealed class Pattern

data class OneOfPattern(
	val oneOf: OneOf) : Pattern() {
	//override fun toString() = reflect.string
}

data class RecursionPattern(
	val recursion: Recursion) : Pattern() {
	//override fun toString() = reflect.string
}

fun pattern(oneOf: OneOf): Pattern =
	OneOfPattern(oneOf)

fun pattern(recursion: Recursion): Pattern =
	RecursionPattern(recursion)

// === matching

val Script<Pattern>.patternOrNull: Pattern?
	get() =
		valueOrNull

fun Script<Nothing>.matches(patternScript: Script<Pattern>): Boolean =
	matches(patternScript, null)

fun Script<Nothing>.matches(pattern: Pattern, backTraceOrNull: BackTrace?): Boolean =
	when (pattern) {
		is OneOfPattern -> matches(pattern, backTraceOrNull)
		is RecursionPattern -> matches(pattern, backTraceOrNull)
	}

fun Script<Nothing>.matches(pattern: OneOfPattern, backTraceOrNull: BackTrace?): Boolean =
	matches(pattern.oneOf, backTraceOrNull)

fun Script<Nothing>.matches(pattern: RecursionPattern, backTraceOrNull: BackTrace?): Boolean =
	backTraceOrNull != null && pattern.recursion.apply(backTraceOrNull)?.let { newBackTraceOrNull ->
		matches(newBackTraceOrNull.patternScript, newBackTraceOrNull.back)
	} ?: false

fun Script<Nothing>?.orNullMatches(patternScript: Script<Pattern>?, backTraceOrNull: BackTrace?): Boolean =
	if (this == null) patternScript == null
	else patternScript != null && matches(patternScript, backTraceOrNull)

fun Script<Nothing>.matches(patternScript: Script<Pattern>, backTraceOrNull: BackTrace?): Boolean =
	patternScript.patternOrNull?.let { pattern ->
		matches(pattern, backTraceOrNull.push(patternScript))
	} ?: when (patternScript) {
		is MetaScript -> false
		is InvokeScript -> this is InvokeScript && this.matches(patternScript, backTraceOrNull)
	}

fun Word.matches(patternWord: Word): Boolean =
	this == patternWord

fun InvokeScript<Nothing>.matches(patternScript: InvokeScript<Pattern>, backTraceOrNull: BackTrace?): Boolean =
	word.matches(patternScript.word) &&
		rhsOrNull.orNullMatches(patternScript.rhsOrNull, backTraceOrNull.push(patternScript)) &&
		lhsOrNull.orNullMatches(patternScript.lhsOrNull, backTraceOrNull)

// === parsing
//
//val Script<Nothing>.parsePattern: Pattern?
//	get() =
//		structureScriptOrNull
//			?.run {
//				true.fold(fieldStream) { field ->
//					this && field.key == eitherWord
//				}.let { isOneOf ->
//					if (!isOneOf) null
//					else fieldStream.run {
//						first.value.parsePatternScript.onlyStack.fold(nextOrNull) { field ->
//							push(field.value.parsePatternScript)
//						}.oneOfPattern
//					}
//				}
//			}
//
//val Script<Nothing>.parsePatternScript: Script<Pattern>
//	get() =
//		when (this) {
//			is MetaScript -> fail
//			is WordScript -> word.term
//			is StructureScript -> parsePattern?.meta?.term ?: parseStructurePatternScript
//		}
//
//val StructureScript<Nothing>.parseStructurePatternScript: Script<Pattern>
//	get() =
//		fieldStream.run {
//			first.parsePatternField.structure.fold(nextOrNull) { field ->
//				plus(field.parsePatternField)
//			}.term
//		}
//
//val Field<Nothing>.parsePatternField: Field<Pattern>
//	get() =
//		key fieldTo value.parsePatternScript

//// === reflect
//
//val Pattern.reflect: Field<Nothing>
//	get() =
//		patternWord fieldTo when (this) {
//			is OneOfPattern -> patternScriptStream.reflect(eitherWord) { reflectMeta(Pattern::reflect) }
//			is RecursePattern -> recurse.reflect
//		}
