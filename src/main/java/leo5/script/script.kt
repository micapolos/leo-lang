package leo5.script

import leo.base.*

sealed class Script
data class EmptyScript(val empty: Empty) : Script()
data class NonEmptyScript(val nonEmpty: ScriptNonEmpty) : Script()

fun script(empty: Empty): Script = EmptyScript(empty)
fun script(scriptNonEmpty: ScriptNonEmpty): Script = NonEmptyScript(scriptNonEmpty)

fun script(vararg lines: Line) = script(empty).foldRight(lines) { script(nonEmpty(this, it)) }
fun script(string: String) = script(string lineTo script(empty))

val Script.emptyOrNull get() = (this as? EmptyScript)?.empty
val Script.nonEmptyOrNull get() = (this as? NonEmptyScript)?.nonEmpty

val Script.string get() = appendableString { it.append(this) }

fun <T> Script.matchEmpty(fn: () -> T) = when (this) {
	is EmptyScript -> fn()
	is NonEmptyScript -> fail()
}

fun <T> Script.matchNonEmpty(fn: (Script) -> T): T = when (this) {
	is EmptyScript -> fail()
	is NonEmptyScript -> fn(this)
}

fun <T> Script.matchNonEmpty(string: String, fn: (Script, Script) -> T): T = when (this) {
	is EmptyScript -> fail()
	is NonEmptyScript -> failIfOr(nonEmpty.line.string != string) {
		fn(nonEmpty.line.script, nonEmpty.script)
	}
}

fun <T> Script.matchLine(fn: (String, Script) -> T) = when (this) {
	is EmptyScript -> fail()
	is NonEmptyScript -> nonEmpty.script.matchEmpty {
		fn(nonEmpty.line.string, nonEmpty.line.script)
	}
}

fun <T> Script.match(string: String, fn: (Script) -> T) =
	matchNonEmpty(string) { lhs, rhs ->
		rhs.matchEmpty { fn(lhs) }
	}

fun <T> Script.match(string1: String, string2: String, fn: (Script, Script) -> T) =
	matchNonEmpty(string1) { lhs1, rhs1 ->
		rhs1.matchNonEmpty(string2) { lhs2, rhs2 ->
			rhs2.matchEmpty {
				fn(lhs1, lhs2)
			}
		}
	}

fun <T> Script.match(string1: String, string2: String, string3: String, fn: (Script, Script, Script) -> T) =
	matchNonEmpty(string1) { lhs1, rhs1 ->
		rhs1.matchNonEmpty(string2) { lhs2, rhs2 ->
			rhs2.matchNonEmpty(string3) { lhs3, rhs3 ->
				rhs3.matchEmpty {
					fn(lhs1, lhs2, lhs3)
				}
			}
		}
	}
