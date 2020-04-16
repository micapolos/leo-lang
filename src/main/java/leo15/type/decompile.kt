package leo15.type

import leo.base.*
import leo14.*
import leo14.untyped.typed.valueJavaScriptLine
import leo15.lambda.*

val Typed.isEmpty: Boolean
	get() =
		type.isEmpty

val Typed.decompileLink: TypedLink?
	get() =
		type.linkOrNull?.let { typeLink ->
			expression.decompilePair(typeLink.lhs.isStatic, typeLink.choice.isStatic).let { termPair ->
				termPair.first.of(typeLink.lhs) linkTo termPair.second.of(typeLink.choice)
			}
		}

fun Typed.decompileRecurse(recursive: Recursive): Typed? =
	notNullIf(type.isRecurse) { expression of recursive.type }

//val TypedChoice.decompileLine: TypedLine
//	get() =
//		expression
//			.unsafeUnchoice(choice.lineCount)
//			.runIndexToValue { it of choice.lineSeq[this]!! }
//
val TypedLine.decompileField: TypedField?
	get() =
		expression.applyOrNull(typeLine.fieldOrNull) { of(it) }

val TypedLine.decompileArrow: TypedArrow?
	get() =
		expression.applyOrNull(typeLine.arrowOrNull) { of(it) }

val TypedLine.decompileLiteral: Literal?
	get() =
		typeLine.literalOrNull

//val TypedLine.decompileJavaTerm: Term?
//	get() =
//		notNullIf(typeLine.isJava) { expression }

// === decompile script ===

val termScriptRecursiveParameter: Parameter<Recursive?> = parameter(null)

fun Term.script(type: Type) =
	when (type) {
		EmptyType -> emptyScript
		is LinkType -> script(type.link)
		is RepeatingType -> script(type.repeating)
		is RecursiveType -> script(type.recursive)
		RecurseType -> recurseScript
	}

fun Term.script(link: TypeLink): Script =
	unsafeUnpair.run {
		first.script(link.lhs).plus(second.scriptLine(link.choice))
	}

fun Term.script(repeating: Repeating): Script =
	script().unsafeFoldRight(this) {
		plus(it.scriptLine(repeating.choice))
	}

fun Term.script(recursive: Recursive): Script =
	termScriptRecursiveParameter.runWith(recursive) { script(recursive.type) }

val Term.recurseScript: Script
	get() =
		script(termScriptRecursiveParameter.value!!.type)

fun Term.scriptLine(choice: Choice): ScriptLine =
	unsafeUnchoice(choice.lineCount).run {
		value.scriptLine(choice.lineSeq[index]!!)
	}

fun Term.scriptLine(typeLine: TypeLine): ScriptLine =
	when (typeLine) {
		is LiteralTypeLine -> line(typeLine.literal)
		is FieldTypeLine -> scriptLine(typeLine.field)
		is ArrowTypeLine -> typeLine.arrow.scriptLine
		JavaTypeLine -> value.valueJavaScriptLine
	}

fun Term.scriptLine(typeField: TypeField): ScriptLine =
	typeField.name lineTo script(typeField.rhs)
