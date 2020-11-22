package leo21.compiled

import leo.base.ifOrNull
import leo21.type.isEmpty

fun Compiled.matchInfix(name: String, fn: (Compiled, Compiled) -> Compiled?): Compiled? =
	linkOrNull?.let { link ->
		link.lineCompiled.fieldCompiledOrNull?.let { field ->
			ifOrNull(field.field.name == name) {
				fn(link.compiled, field.rhsCompiled)
			}
		}
	}

fun Compiled.matchPrefix(name: String, fn: (Compiled) -> Compiled?): Compiled? =
	matchInfix(name) { lhs, rhs ->
		lhs.matchEmpty {
			fn(rhs)
		}
	}

fun Compiled.matchPostfix(name: String, fn: (Compiled) -> Compiled?): Compiled? =
	matchInfix(name) { lhs, rhs ->
		rhs.matchEmpty {
			fn(lhs)
		}
	}

fun Compiled.match(name: String, fn: () -> Compiled?): Compiled? =
	matchInfix(name) { lhs, rhs ->
		lhs.matchEmpty {
			rhs.matchEmpty {
				fn()
			}
		}
	}

fun Compiled.matchText(fn: (Compiled) -> Compiled?): Compiled? =
	onlyLineOrNull?.stringCompiledOrNull?.let { fn(this) }

fun Compiled.matchNumber(fn: (Compiled) -> Compiled?): Compiled? =
	onlyLineOrNull?.doubleCompiledOrNull?.let { fn(this) }

fun Compiled.matchEmpty(fn: () -> Compiled?): Compiled? =
	ifOrNull(type.isEmpty) {
		fn()
	}
