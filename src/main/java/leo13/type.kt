package leo13

import leo.base.fold
import leo.base.ifOrNull
import leo.base.indexed
import leo.base.notNullIf
import leo9.*

data class Type(val functionTypeOrNull: FunctionType?, val choiceStack: Stack<Choice>)
data class Choice(val lineStack: Stack<TypeLine>)
data class TypeLine(val name: String, val rhs: Type)
data class TypeArrow(val lhs: Type, val rhs: Type)
data class TypeAccess(val int: Int, val type: Type)

// --- constructors

val Stack<Choice>.type get() = Type(null, this)
fun Type.plus(choice: Choice) = copy(choiceStack = choiceStack.push(choice))
fun Type.plus(line: TypeLine) = plus(choice(line))
fun type(vararg choices: Choice) = stack(*choices).type
fun type(line: TypeLine, vararg lines: TypeLine) = type().plus(line).fold(lines) { plus(it) }
fun FunctionType?.type(vararg choices: Choice) = Type(this, stack(*choices))
infix fun Type.arrowTo(rhs: Type) = TypeArrow(this, rhs)
fun access(int: Int, type: Type) = TypeAccess(int, type)

val Stack<TypeLine>.choice get() = Choice(this)
fun choice(vararg lines: TypeLine) = stack(*lines).choice
fun Choice.plus(line: TypeLine) = lineStack.push(line).choice

infix fun String.lineTo(rhs: Type) = TypeLine(this, rhs)

val Type.isEmpty get() = choiceStack.isEmpty
val Type.onlyFunctionTypeOrNull get() = ifOrNull(choiceStack.isEmpty) { functionTypeOrNull }

val Type.onlyLineOrNull
	get() =
		onlyChoiceOrNull?.onlyLineOrNull

val Type.onlyChoiceStackOrNull
	get() =
		notNullIf(functionTypeOrNull == null) {
			choiceStack
		}

val Type.onlyChoiceOrNull
	get() =
		ifOrNull(functionTypeOrNull == null) {
			choiceStack.onlyOrNull
		}

val Choice.onlyLineOrNull
	get() =
		lineStack.onlyOrNull

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

val Type.accessOrNull
	get(): TypeAccess? =
		onlyLineOrNull?.accessOrNull

val TypeLine.accessOrNull
	get(): TypeAccess? =
		rhs.accessOrNull(name)

fun Type.accessOrNull(name: String): TypeAccess? =
	onlyLineOrNull
		?.rhs
		?.indexedRhsOrNull(name)
		?.let { access(it.index, type(choice(name lineTo it.value))) }

fun Type.indexedRhsOrNull(name: String): IndexedValue<Type>? =
	onlyChoiceStackOrNull?.indexed?.mapOnly {
		value.onlyRhsOrNull(name)?.let { rhs ->
			index indexed rhs
		}
	}

fun Choice.onlyRhsOrNull(name: String) =
	onlyLineOrNull?.rhsOrNull(name)

fun TypeLine.rhsOrNull(name: String) =
	notNullIf(this.name == name) { rhs }

