package leo13.untyped

import leo.base.fold
import leo.base.ifOrNull
import leo13.script.Script
import leo13.script.ScriptLine
import leo13.script.lineSeq
import leo13.script.onlyLineOrNull
import leo13.script.script
import leo13.scripter.script
import leo13.scripter.scriptLine
import leo13.scripter.toString
import leo9.*

val patternType: leo13.scripter.Scripter<Pattern> = leo13.scripter.Scripter(
	"pattern",
	{
		choiceOrNull.run { choiceType.script(this) }
			.orIfNull { script() }
			.fold(lineStack.reverse.seq) { plus(patternLineType.scriptLine(it)) }
	},
	{
		pattern().fold(lineSeq) { unsafePlus(it) }
	})

data class Pattern(val seed: PatternSeed, val lineStack: Stack<PatternLine>) {
	override fun toString() = patternType.toString(this)
}

fun pattern(choiceOrNull: Choice?, lineStack: Stack<PatternLine>) =
	Pattern(choiceOrNull, lineStack)

fun pattern(vararg lines: PatternLine) = pattern(null, stack(*lines))
fun pattern(choice: Choice, vararg lines: PatternLine) = pattern(choice, stack(*lines))
fun Pattern.plus(line: PatternLine) = pattern(choiceOrNull, lineStack.push(line))

val Pattern.isAny: Boolean
	get() =
		script == script("any")

fun Pattern.unsafePlus(scriptLine: ScriptLine): Pattern =


fun Script.matches(pattern: Pattern): Boolean =
	pattern.isAny || matchesPlain(pattern.script)

fun Script.matches(script: Script): Boolean =
	matches(Pattern(script))

fun Script.matchesChoiceOrNull(script: Script): Boolean? =
	script
		.onlyLineOrNull
		?.let { line ->
			ifOrNull(line.name == "choice") {
				matchesCasesOrNull(line.rhs)
			}
		}

fun ScriptLine.matchesCasesOrNull(cases: Script): Boolean? =
	cases.lineStack.mapOrNull { }

fun ScriptLine.matchesCaseOrNull(scriptLine: ScriptLine): Boolean? =
	ifOrNull(scriptLine.name == "case") {
		scriptLine.rhs.onlyLineOrNull?.let { line ->
			ifOrNull(name == line.name) {
				rhs.matches(line.rhs)
			}
		}
	}

fun Script.matchesPlain(pattern: Script): Boolean =
	when (pattern.lineStack) {
		is EmptyStack -> lineStack is EmptyStack
		is LinkStack -> lineStack is LinkStack
			&& lineStack.link.value.matches(pattern.lineStack.link.value)
			&& lineStack.link.stack.script.matches(Pattern(pattern.lineStack.link.stack.script))
	}

fun ScriptLine.matches(patternLine: ScriptLine): Boolean =
	name == patternLine.name && rhs.matches(Pattern(patternLine.rhs))
