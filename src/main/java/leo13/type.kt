package leo13

import leo.base.ifOrNull
import leo.base.notNullIf
import leo9.*

data class Type(val headFunctionTypeOrNull: FunctionType?, val choiceStack: Stack<Choice>)
data class Choice(val lineStack: Stack<TypeLine>)
data class TypeLine(val name: String, val rhs: Type)
data class TypeArrow(val lhs: Type, val rhs: Type)
data class TypeAccess(val int: Int, val type: Type)

// --- constructors

val Stack<Choice>.type get() = Type(null, this)
fun Type.plus(choice: Choice) = copy(choiceStack = choiceStack.push(choice))
fun type(vararg choices: Choice) = stack(*choices).type
fun FunctionType?.type(vararg choices: Choice) = Type(this, stack(*choices))
infix fun Type.arrowTo(rhs: Type) = TypeArrow(this, rhs)

val Stack<TypeLine>.choice get() = Choice(this)
fun choice(vararg lines: TypeLine) = stack(*lines).choice
fun Choice.plus(line: TypeLine) = lineStack.push(line).choice

infix fun String.lineTo(rhs: Type) = TypeLine(this, rhs)

val Type.isEmpty get() = choiceStack.isEmpty
val Type.functionTypeOrNull get() = ifOrNull(choiceStack.isEmpty) { headFunctionTypeOrNull }
fun access(int: Int, type: Type) = TypeAccess(int, type)

// --- script -> type

val Script.type get() = eitherTypeOrNull ?: exactType

val Script.eitherTypeOrNull: Type?
	get() =
		lineStack
			.mapOrNull { eitherTypeLineOrNull }
			?.let { typeLineStack ->
				ifOrNull(!typeLineStack.isEmpty) {
					type(typeLineStack.choice)
				}
		}

val Script.exactType
	get() =
		lineStack.map { choice(typeLine) }.type

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
	zip(choiceStack, value.lineStack).map { first!!.scriptLine(second!!) }.script

fun Choice.scriptLine(valueLine: ValueLine): ScriptLine =
	lineStack.get(valueLine.int)!!.scriptLine(valueLine.rhs)

fun TypeLine.scriptLine(value: Value): ScriptLine =
	name lineTo rhs.script(value)

// --- script -> value

fun Type.value(script: Script): Value =
	zip(choiceStack, script.lineStack).map { first!!.valueLine(second!!) }.value

fun Choice.valueLine(scriptLine: ScriptLine): ValueLine =
	lineStack.indexed.mapFirst { value.valueLineOrNull(scriptLine, index) }!!

fun TypeLine.valueLineOrNull(scriptLine: ScriptLine, int: Int): ValueLine? =
	notNullIf(name == scriptLine.name) {
		int lineTo rhs.value(scriptLine.rhs)
	}

// --- access

//fun Type.rhsAccessOrNull(name: String): TypeAccess? =
//	choiceStack.onlyOrNull?.let { choice ->
//		choice.lineStack.onlyOrNull?.let { line ->
//			line.rhs.let { rhs ->
//				notNullIf(rhs.headFunctionTypeOrNull == null) {
//					rhs.choiceStack.mapOnly {
//						choice.lineStack.linkOrNull?.let { link ->
//							orNullIf(link.stack.isEmpty) {
//								notNullIf(link.value.name == name) {
//									link.value
//								}
//							}
//						}
//					}
//				}
//			}
//		}
//	}