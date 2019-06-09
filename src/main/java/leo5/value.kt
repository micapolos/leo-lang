package leo5

import leo.base.appendableString
import leo.base.empty
import leo.base.fold

data class Value(val script: Script, val type: Type) {
	override fun toString() = appendableString { it.append(this) }
}

fun value(script: Script, type: Type = type(self)) = Value(script, type)
fun Value.plus(vararg lines: Line) = fold(lines) { plus(it) }
fun value(vararg lines: Line) = value(script(empty)).plus(*lines)

fun Value.plus(line: Line): Value =
	plus(
		line,
		if (type.isSelf && line.value.type.isSelf) type(self)
		else type(typeValue.plus(line(line.name, line.value.typeValue))))

fun Value.plus(line: Line, type: Type): Value = value(script(application(this, line)), type)

val Value.typeValue: Value get() = type.valueOrNull ?: this

infix fun Script.of(type: Type) = value(this, type)
infix fun Script.of(self: Self) = this of type(self)
infix fun Script.of(value: Value) = this of type(value)

fun Value.invoke(argument: Value) = script.invoke(argument)

fun Appendable.append(value: Value): Appendable = append(value.script)