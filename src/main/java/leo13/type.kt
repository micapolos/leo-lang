package leo13

import leo.base.ifOrNull
import leo.base.notNullIf
import leo9.*

data class Type(val choiceStack: Stack<Choice>)
data class Choice(val lineStack: Stack<TypeLine>)
data class TypeLine(val name: String, val rhs: Type)

// --- constructors

fun type(choiceStack: Stack<Choice>) = Type(choiceStack)
fun type(vararg lines: Choice) = Type(stack(*lines))
fun Type.plus(choice: Choice) = Type(choiceStack.push(choice))

fun choice(lineStack: Stack<TypeLine>) = Choice(lineStack)
fun choice(vararg lines: TypeLine) = Choice(stack(*lines))
fun Choice.plus(line: TypeLine) = Choice(lineStack.push(line))

infix fun String.lineTo(rhs: Type) = TypeLine(this, rhs)

// --- parse

val Script.type get() = eitherTypeOrNull ?: exactType

val Script.eitherTypeOrNull: Type?
	get() =
		lineStack
			.mapOrNull { eitherTypeLineOrNull }
			?.let { typeLineStack ->
				ifOrNull(!typeLineStack.isEmpty) {
					type(choice(typeLineStack))
				}
		}

val Script.exactType
	get() =
		type(lineStack.map { choice(typeLine) })

val ScriptLine.eitherTypeLineOrNull: TypeLine?
	get() =
		ifOrNull(name == "either") {
			rhs.onlyLineOrNull?.typeLine
		}

val ScriptLine.typeLine: TypeLine
	get() =
		name lineTo rhs.type

// --- value -> script

fun Type.script(value: Value): Script =
	script(zip(choiceStack, value.lineStack).map { first!!.scriptLine(second!!) })

fun Choice.scriptLine(valueLine: ValueLine): ScriptLine =
	lineStack.get(valueLine.int)!!.scriptLine(valueLine.rhs)

fun TypeLine.scriptLine(value: Value): ScriptLine =
	name lineTo rhs.script(value)

// --- script -> value

fun Type.value(script: Script): Value =
	value(zip(choiceStack, script.lineStack).map { first!!.valueLine(second!!) })

fun Choice.valueLine(scriptLine: ScriptLine): ValueLine =
	lineStack.indexed.mapFirst { value.valueLineOrNull(scriptLine, index) }!!

fun TypeLine.valueLineOrNull(scriptLine: ScriptLine, int: Int): ValueLine? =
	notNullIf(name == scriptLine.name) {
		int lineTo rhs.value(scriptLine.rhs)
	}