package leo13.untyped

import leo.base.notNullIf
import leo.base.orNullFold
import leo13.LeoStruct
import leo13.script.Script
import leo13.script.ScriptLine
import leo13.script.isEmpty
import leo13.script.lineSeq

data class switchParser(
	val switch: switch = switch(),
	val nameOrNull: name? = null) : LeoStruct("parser", switch, nameOrNull) {
	override fun toString() = super.toString()
}

fun switchParser.plus(line: ScriptLine): switchParser? =
	if (nameOrNull == null)
		notNullIf(line.rhs.isEmpty) { switchParser(switch, name(line.name)) }
	else notNullIf(line.name == "gives") {
		switchParser(
			if (nameOrNull.string == "else") switch.plusElse(line.rhs)
			else switch.plus(case(nameOrNull, line.rhs)))
	}

val switchParser.parsedSwitchOrNull: switch?
	get() =
		notNullIf(nameOrNull == null) { switch }

val Script.parseSwitch: switch?
	get() =
		switchParser()
			.orNullFold(lineSeq) { plus(it) }
			?.parsedSwitchOrNull