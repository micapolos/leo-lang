package leo13.untyped

import leo.base.ifOrNull
import leo.base.notNullIf
import leo.base.orNullFold
import leo13.EmptyStack
import leo13.LinkStack
import leo13.mapFirst
import leo13.push
import leo13.script.*

fun Script.firstRhsOrNull(name: String): Script? =
	lineStack.mapFirst { rhsOrNull(name) }

fun ScriptLine.rhsOrNull(name: String): Script? =
	notNullIf(this.name == name) { rhs }

fun ScriptLine.has(name: String): Boolean =
	this.name == name

fun Script.setFirstRhsOrNull(line: ScriptLine): Script? =
	when (lineStack) {
		is EmptyStack -> null
		is LinkStack -> lineStack.link.value.setRhsOrNull(line)
			?.let { lineStack.link.stack.push(it).script }
			?: lineStack.link.stack.script.setFirstRhsOrNull(line)?.plus(lineStack.link.value)
	}

fun ScriptLine.setRhsOrNull(line: ScriptLine): ScriptLine? =
	notNullIf(name == line.name) { line }

fun Script.normalizedLineOrNull(line: ScriptLine): ScriptLine? =
	notNullIf(line.rhs.isEmpty) { line.name lineTo this }

fun Script.resolve(line: ScriptLine): Script =
	resolveOrNull(line) ?: plus(line)

fun Script.resolveOrNull(line: ScriptLine): Script? =
	when (line.name) {
		"set" -> resolveSetOrNull(line.rhs)
		"body" -> resolveBodyOrNull(line.rhs)
		else -> resolveGetOrNull(line)
	}

fun Script.resolveGetOrNull(line: ScriptLine): Script? =
	ifOrNull(isEmpty) {
		line.rhs.linkOrNull?.line?.let { line2 ->
			line2.rhs.firstRhsOrNull(line.name)?.let { rhs ->
				script(line.name lineTo rhs)
			}
		}
	}

fun Script.resolveSetOrNull(rhs: Script): Script? =
	linkOrNull?.let { link ->
		link.line.let { line ->
			line.rhs
				.orNullFold(rhs.lineSeq) { setFirstRhsOrNull(it) }
				?.let { setLine -> link(link.lhs, line.name lineTo setLine).script }
		}
	}

fun Script.resolveBodyOrNull(rhs: Script): Script? =
	ifOrNull(isEmpty) { rhs.onlyLineOrNull?.rhs }

val Script.evaluate
	get() =
		evaluator().plus(this).evaluated.script

val Script.interpret
	get() =
		interpreter().plus(this).evaluator.evaluated.script

val Script.normalize: Script
	get() =
		normalizer().plus(this).normalized.script

val ScriptLine.normalize: ScriptLine
	get() =
		name lineTo rhs.normalize