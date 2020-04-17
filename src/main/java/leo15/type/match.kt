package leo15.type

import leo.base.ifOrNull
import leo15.numberName
import leo15.textName

fun Typed.matchInfix(name: String, fn: (Typed, Typed) -> Typed?): Typed? =
	linkOrNull?.let { link ->
		link.choice.onlyLineOrNull?.fieldOrNull?.let { field ->
			ifOrNull(field.field.name == name) {
				fn(link.lhs, field.rhs)
			}
		}
	}

fun Typed.matchPrefix(name: String, fn: (Typed) -> Typed?): Typed? =
	matchInfix(name) { _, rhs -> fn(rhs) }

fun Typed.matchEmpty(name: String, fn: () -> Typed?): Typed? =
	matchPrefix(name) { fn() }

fun Typed.matchJava(fn: (Expression) -> Typed?): Typed? =
	onlyLineOrNull?.let { line ->
		ifOrNull(line.typeLine.isJava) {
			fn(line.expression)
		}
	}

fun Typed.matchText(fn: (Expression) -> Typed?): Typed? =
	matchPrefix(textName) { rhs ->
		rhs.matchJava(fn)
	}

fun Typed.matchNumber(fn: (Expression) -> Typed?): Typed? =
	matchPrefix(numberName) { rhs ->
		rhs.matchJava(fn)
	}