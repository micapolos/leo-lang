package leo13.untyped

import leo.base.notNullIf
import leo.base.orNullFold
import leo13.LeoStruct
import leo13.script.Script
import leo13.script.ScriptLine
import leo13.script.isEmpty
import leo13.script.lineSeq

data class SwitchParser(
	val switch: Switch,
	val nameOrNull: Name?) : LeoStruct("parser", switch, nameOrNull) {
	override fun toString() = super.toString()
}

fun parser(switch: Switch, nameOrNull: Name? = null) = SwitchParser(switch, nameOrNull)

fun SwitchParser.plus(line: ScriptLine): SwitchParser? =
	if (nameOrNull == null)
		notNullIf(line.rhs.isEmpty) { SwitchParser(switch, Name(line.name)) }
	else notNullIf(line.name == "gives") {
		parser(
			if (nameOrNull.string == "else") switch.plusElse(line.rhs)
			else switch.plus(Case(nameOrNull, line.rhs)))
	}

val SwitchParser.parsedSwitchOrNull: Switch?
	get() =
		notNullIf(nameOrNull == null) { switch }

val Script.parseSwitch: Switch?
	get() =
		parser(switch())
			.orNullFold(lineSeq) { plus(it) }
			?.parsedSwitchOrNull