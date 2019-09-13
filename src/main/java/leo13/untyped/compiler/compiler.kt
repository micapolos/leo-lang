package leo13.untyped.compiler

import leo.base.ifOrNull
import leo13.script.Script
import leo13.script.ScriptLine
import leo13.script.isEmpty
import leo13.untyped.*
import leo13.untyped.expression.get
import leo13.untyped.expression.op
import leo13.untyped.expression.plus
import leo13.untyped.expression.set
import leo13.untyped.pattern.choiceOrNull
import leo13.untyped.pattern.getOrNull
import leo13.untyped.pattern.isEmpty
import leo13.untyped.pattern.setOrNull
import leo9.linkOrNull
import leo9.onlyOrNull

data class Compiler(
	val context: Context,
	val compiled: Compiled)

fun compiler(arrows: Context, compiled: Compiled) =
	Compiler(arrows, compiled)

fun Compiler.plus(line: ScriptLine): Compiler =
	if (isError) this
	else if (line.rhs.isEmpty) plus(line.name)
	else plusNormalized(line)

fun Compiler.plus(name: String): Compiler =
	compiler(context, compiled()).plus(name lineTo compiled)

fun Compiler.plusNormalized(line: ScriptLine): Compiler =
	when (line.name) {
		defineName -> plusDefineOrNull(line.rhs)
		quoteName -> plusQuoteOrNull(line.rhs)
		quoteName -> plusUnquoteOrNull(line.rhs)
		scriptName -> plusScriptOrNull(line.rhs)
		switchName -> plusSwitch(line.rhs)
		else -> plusOtherOrNull(line)
	} ?: TODO()

fun Compiler.plusDefineOrNull(rhs: Script): Compiler? =
	TODO()

fun Compiler.plusQuoteOrNull(rhs: Script): Compiler? =
	TODO()

fun Compiler.plusUnquoteOrNull(rhs: Script): Compiler? =
	TODO()

fun Compiler.plusScriptOrNull(rhs: Script): Compiler? =
	TODO()

fun Compiler.plusSwitch(rhs: Script): Compiler =
	TODO()
//	plus(context.compileSwitch(rhs))

fun Compiler.plus(switch: CompiledSwitch): Compiler =
	TODO()
//	compiler(
//		context,
//		compiled)
//	context
//		.compileSwitch(rhs)
//		.let { switch ->
//			compiler(
//				context,
//				context.compileSwitch(rhs))
//		}

fun Compiler.plusOtherOrNull(line: ScriptLine): Compiler? =
	context.compile(line).let { plus(it) }

fun Compiler.plus(line: CompiledLine): Compiler =
	when (line.name) {
		setName -> plusSetOrNull(line.rhs)
		forgetName -> plusForgetOrNull(line.rhs)
		replaceName -> plusReplace(line.rhs)
		previousName -> plusPreviousOrNull(line.rhs)
		else -> plusOther(line)
	} ?: plusError(line)

fun Compiler.plusSetOrNull(rhs: Compiled): Compiler? =
	TODO()
//	rhs.pattern.lineStackOrNull?.let { lineStack ->
//		orNullFold(lineStack.reverse.seq) { plusSet(it) }
//	}

fun Compiler.plusReplace(rhs: Compiled): Compiler =
	TODO()

fun Compiler.plusSetOrNull(line: CompiledLine): Compiler? =
	compiled
		.pattern
		.setOrNull(line.patternLine)
		?.let { setPattern ->
			set(
				compiled(
					compiled.expression.plus(set(line.expressionLine).op),
					setPattern))
		}

fun Compiler.plusForgetOrNull(rhs: Compiled): Compiler? =
	ifOrNull(compiled.pattern.isEmpty) {
		compiler(context, compiled())
	}

fun Compiler.plusPreviousOrNull(rhs: Compiled): Compiler? =
	ifOrNull(compiled.pattern.isEmpty) {
		rhs.previousOrNull?.let { compiler(context, it) }
	}

fun Compiler.plusOther(line: CompiledLine): Compiler =
	null
		?: plusGetOrNull(line)
		?: append(line)

fun Compiler.plusGetOrNull(line: CompiledLine): Compiler? =
	ifOrNull(compiled.pattern.isEmpty) {
		line
			.rhs
			.pattern
			.getOrNull(line.name)
			?.let { set(compiled(compiled.expression.plus(get(line.name).op), it)) }
	}

fun Compiler.plusError(line: CompiledLine): Compiler =
	compiler(context, compiled.plus(errorName lineTo compiled(line)))

fun Compiler.append(line: CompiledLine): Compiler =
	set(compiled.plus(line))

fun Compiler.set(compiled: Compiled) =
	copy(compiled = compiled)

val Compiler.isError
	get() =
		compiled.pattern.itemStack.linkOrNull?.value?.choiceOrNull?.eitherStack?.onlyOrNull?.name == errorName