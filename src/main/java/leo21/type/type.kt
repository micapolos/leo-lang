package leo21.type

import leo.base.notNullOrError
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

data class Type(val lineStack: Stack<Line>) : Scriptable() {
	override fun toString() = super.toString()
	override val reflectScriptLine: ScriptLine get() = "type" lineTo script
}

val Type.linkOrNull: Link<Type, Line>?
	get() =
		lineStack.linkOrNull?.run { stack.type linkTo value }

val Link<Type, Line>.type get() = tail
val Link<Type, Line>.line get() = head

val Stack<Line>.type get() = Type(this)
fun Type.plus(line: Line): Type = lineStack.push(line).type
fun type(vararg lines: Line) = stack(*lines).type

val stringType = type(stringLine)
val numberType = type(numberLine)

fun Type.make(name: String): Type =
	type(name lineTo this)

val Type.onlyNameOrNull: String?
	get() =
		lineStack.onlyOrNull?.fieldOrNull?.orNullIf { rhs != type() }?.name

val Type.isEmpty: Boolean get() = lineStack.isEmpty