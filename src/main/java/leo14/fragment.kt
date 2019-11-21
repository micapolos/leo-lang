package leo14

import leo.base.*

data class Fragment(
	val parent: FragmentParent?,
	val script: Script)

data class FragmentParent(
	val fragment: Fragment,
	val begin: Begin)

val emptyFragment = Fragment(null, script())
fun Fragment.parent(begin: Begin) = FragmentParent(this, begin)
val FragmentParent.childFragment get() = Fragment(this, script())
fun Fragment.updateScript(fn: Script.() -> Script) = copy(script = script.fn())

fun Fragment.plus(literal: Literal) = updateScript { plus(line(literal)) }
fun Fragment.plus(begin: Begin) = parent(begin).childFragment
fun Fragment.begin(name: String) = parent(leo14.begin(name)).childFragment
val Fragment.endOrNull get() = parent?.run { fragment.updateScript { plus(begin.string lineTo script) } }
val Fragment.end get() = endOrNull.notNullOrError("$this.end")

fun Fragment.plusOrNull(token: Token): Fragment? =
	when (token) {
		is LiteralToken -> plus(token.literal)
		is BeginToken -> plus(token.begin)
		is EndToken -> endOrNull
	}

fun Fragment.plus(token: Token): Fragment =
	plusOrNull(token).notNullOrError("$this.plus($token)")

// === indentString

val Fragment.indent: Indent
	get() =
		parent?.indent ?: 0.indent

val FragmentParent.indent: Indent
	get() =
		fragment.indent.inc

val Fragment.indentString: String
	get() =
		if (parent == null)
			if (script.isEmpty) ""
			else script.indentString + "\n"
		else
			if (script.isEmpty) parent.indentString + "\n" + indent.string
			else parent.indentString + "\n" + indent.string + script.string(indent) + "\n" + indent.string

val FragmentParent.indentString: String
	get() =
		fragment.indentString + begin.string
