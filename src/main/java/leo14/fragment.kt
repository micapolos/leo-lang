package leo14

import leo.base.failIfOr
import leo.base.ifNotNull
import leo.base.notNullOrError
import leo13.fold
import leo13.reverse

data class Fragment(
	val parent: FragmentParent?,
	val script: Script) {
	override fun toString() = indentString
}

data class FragmentParent(
	val fragment: Fragment,
	val begin: Begin)

val emptyFragment = Fragment(null, script())
fun Fragment.parent(begin: Begin) = FragmentParent(this, begin)
val FragmentParent.childFragment get() = Fragment(this, script())
fun FragmentParent?.fragment(script: Script) = Fragment(this, script)
fun Fragment.updateScript(fn: Script.() -> Script) = copy(script = script.fn())
val Script.fragment get() = Fragment(null, this)

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

val Fragment.reflectScriptLine: ScriptLine
	get() =
		"fragment" lineTo script(
			parent?.reflectScriptLine ?: "parent" lineTo script("null"),
			"script" lineTo script)

val FragmentParent.reflectScriptLine
	get() =
		"fragment" lineTo script(
			fragment.reflectScriptLine,
			begin.reflectScriptLine)

fun Fragment.plus(script: Script): Fragment =
	fold(script.tokenStack.reverse) { plus(it) }

fun Fragment.plus(fragment: Fragment): Fragment =
	this
		.ifNotNull(fragment.parent, Fragment::plus)
		.plus(fragment.script)

fun Fragment.plus(fragmentParent: FragmentParent): Fragment =
	plus(fragmentParent.fragment).plus(fragmentParent.begin)

val Fragment.rootScript: Script
	get() =
		failIfOr(parent != null) { script }

fun FragmentParent.prepend(script: Script): FragmentParent =
	fragment.prepend(script).parent(begin)

fun Fragment.prepend(script: Script): Fragment =
	if (parent == null) parent.fragment(script.plus(this.script))
	else parent.prepend(script).fragment(this.script)
