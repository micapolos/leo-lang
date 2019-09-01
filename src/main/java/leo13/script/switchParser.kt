package leo13.script

import leo13.Scriptable
import leo13.fail
import leo13.failRun

data class SwitchParser(
	val switchNodeOrNull: SwitchNode?,
	val nameOrNull: String?) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "parser"
	override val scriptableBody get() = script() // not relevant
}

fun switchParser(swotchNodeOrNull: SwitchNode?, nameOrNull: String?) = SwitchParser(swotchNodeOrNull, nameOrNull)
fun switchParser() = switchParser(null, null)

fun SwitchParser.push(scriptLine: ScriptLine): SwitchParser =
	if (nameOrNull == null)
		if (!scriptLine.rhs.isEmpty) fail("case" lineTo script(scriptLine))
		else switchParser(switchNodeOrNull, scriptLine.name)
	else
		if (scriptLine.name != "gives") fail("case" lineTo script(nameOrNull lineTo script(), scriptLine))
		else (nameOrNull caseTo scriptLine.rhs).let { case ->
			if (switchNodeOrNull == null) switchParser(switchNode(case), null)
			else switchParser(switchNodeOrNull.plus(case), null)
		}

val SwitchParser.switch: Switch
	get() =
		failRun("switch") {
			if (nameOrNull != null) fail("case" lineTo script(nameOrNull))
			else if (switchNodeOrNull == null) fail(script())
			else when (switchNodeOrNull) {
				is CaseSwitchNode -> fail("single" lineTo script(switchNodeOrNull.case.scriptableLine))
				is SwitchSwitchNode -> switchNodeOrNull.switch
			}
		}
