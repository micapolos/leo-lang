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
		type.linkOrNull?.let { link ->
			term.unsafeUnpair.let { pair ->
				pair.first.of(link.lhs) linkTo pair.second.of(link.choice)
			}
		}

fun Typed.decompileRecurse(recursive: Recursive): Typed? =
	notNullIf(type.isRecurse) { term of recursive.type }

val TypedChoice.decompileLine: TypedLine
	get() =
		term
			.unsafeUnchoice(choice.lineCount)
			.runIndexToValue { it of choice.lineSeq[this]!! }

val TypedLine.decompileField: TypedField?
	get() =
		term.applyOrNull(typeLine.fieldOrNull) { of(it) }

val TypedLine.decompileArrow: TypedArrow?
	get() =
		term.applyOrNull(typeLine.arrowOrNull) { of(it) }

val TypedLine.decompileLiteral: Literal?
	get() =
		typeLine.literalOrNull

val TypedLine.decompileJavaTerm: Term?
	get() =
		notNullIf(typeLine.isJava) { term }

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
