package leo23.typed.value

import leo14.ScriptLine
import leo14.line
import leo14.lineTo
import leo14.literal
import leo14.plus
import leo14.script
import leo14.scriptLine
import leo23.type.ArrowType
import leo23.type.BooleanType
import leo23.type.ChoiceType
import leo23.type.NumberType
import leo23.type.StructType
import leo23.type.TextType
import leo23.type.Type
import leo23.type.scriptLine
import leo23.typed.Typed
import leo23.typed.of
import leo23.value.Value
import leo23.value.indexed
import leo23.value.list
import leo23.value.number
import leo23.value.string

val Typed<Value, Type>.scriptLine: ScriptLine
	get() =
		when (t) {
			BooleanType -> "boolean" lineTo script(if (v as Boolean) "true" else "false")
			TextType -> line(literal(v.string))
			NumberType -> line(literal(v.number))
			is ArrowType -> "function" lineTo t.paramTypes.map { it.scriptLine }.script.plus("does" lineTo script(t.returnType.scriptLine))
			is StructType -> when (t.fields.size) {
				0 -> t.name.scriptLine
				1 -> t.name lineTo v.of(t.fields[0]).scriptLine.script
				else -> t.name lineTo v.list.zip(t.fields) { v, t -> v.of(t).scriptLine }.script
			}
			is ChoiceType -> t.name lineTo v.indexed.run { value.of(t.cases[index]).scriptLine }.script
		}