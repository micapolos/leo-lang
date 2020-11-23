package leo21.type

import leo.base.fold
import leo.base.orNullIf
import leo13.Link
import leo13.Stack
import leo13.isEmpty
import leo13.linkOrNull
import leo13.linkTo
import leo13.onlyOrNull
import leo13.push
import leo13.stack
import leo14.ScriptLine
import leo14.Scriptable
import leo14.lineTo

interface AsType {
	val asType: Type
}

data class Type(val lineStack: Stack<Line>) : Scriptable(), AsType {
	override fun toString() = super.toString()
	override val reflectScriptLine: ScriptLine get() = "type" lineTo script
	override val asType get() = this
}

val Type.linkOrNull: Link<Type, Line>?
	get() =
		lineStack.linkOrNull?.run { stack.type linkTo value }

val Link<Type, Line>.type get() = tail
val Link<Type, Line>.line get() = head

val Stack<Line>.type get() = Type(this)
val emptyType = Type(stack())
fun Type.plus(asLine: AsLine): Type = lineStack.push(asLine.asLine).type
fun type(vararg asLines: AsLine) = emptyType.fold(asLines) { plus(it) }

val stringType = type(stringLine)
val numberType = type(numberLine)
val equalsType = type(equalsLine)

fun Type.make(name: String): Type =
	type(name lineTo this)

val Type.onlyNameOrNull: String?
	get() =
		lineStack.onlyOrNull?.fieldOrNull?.orNullIf { rhs != type() }?.name

val Type.isEmpty: Boolean get() = lineStack.isEmpty