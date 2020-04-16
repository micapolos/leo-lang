package leo15.type

import leo.base.ifOrNull

fun Typed.matchInfix(name: String, fn: (Typed, Typed) -> Typed): Typed? =
	linkOrNull?.let { link ->
		link.choice.onlyLineOrNull?.fieldOrNull?.let { field ->
			ifOrNull(field.field.name == name) {
				fn(link.lhs, field.rhs)
			}
		}
	}

fun Typed.matchPrefix(name: String, fn: (Typed) -> Typed): Typed? =
	matchInfix(name) { _, rhs -> fn(rhs) }

fun Typed.matchEmpty(name: String, fn: () -> Typed): Typed? =
	matchPrefix(name) { fn() }
