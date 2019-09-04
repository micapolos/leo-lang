package leo13.untyped

import leo.base.ifOrNull
import leo.base.notNullIf
import leo.base.orNullFold
import leo13.script.*
import leo9.EmptyStack
import leo9.LinkStack
import leo9.mapFirst
import leo9.push

fun Script.firstRhsOrNull(name: String): Script? =
	lineStack.mapFirst { rhsOrNull(name) }

fun ScriptLine.rhsOrNull(name: String): Script? =
	notNullIf(this.name == name) { rhs }

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
		else -> resolveGetOrNull(line)
	}

fun Script.resolveGetOrNull(line: ScriptLine): Script? =
	ifOrNull(isEmpty) {
		line.rhs.onlyLineOrNull?.let { line2 ->
			line2.rhs.firstRhsOrNull(line.name)?.let { rhs ->
				script(line.name lineTo rhs)
			}
		}
	}

fun Script.resolveSetOrNull(rhs: Script): Script? =
	onlyLineOrNull?.let { line ->
		line.rhs
			.orNullFold(rhs.lineSeq) { setFirstRhsOrNull(it) }
			?.let { setLine -> script(line.name lineTo setLine) }
	}

fun Script.resolveCaseOrNull(cases: Script): Script? =
	onlyLineOrNull?.rhs?.caseOrNull(cases)

fun Script.caseOrNull(cases: Script): Script? =
	onlyLineOrNull?.let { line ->
		cases.linkOrNull?.let { cases ->
			line.caseOrNull(cases)
		}
	}

fun ScriptLine.caseOrNull(cases: Script): Script? =
	cases.linkOrNull?.let { caseOrNull(it) }

fun ScriptLine.caseOrNull(cases: ScriptLink): Script? =
	ifOrNull(cases.line.name == "case") {
		cases.line.rhs.onlyLineOrNull?.let { caseLine ->
			if (name == caseLine.name) script(caseLine)
			else caseOrNull(cases.lhs)
		}
	}
