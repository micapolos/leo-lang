package leo.lab.v2

import leo.base.fold

data class Recursion(
	val jump: Jump,
	val recursionOrNull: Recursion?)

fun Recursion?.plus(jump: Jump): Recursion =
	Recursion(jump, this)

fun recursion(jump: Jump, vararg jumps: Jump): Recursion =
	Recursion(jump, null).fold(jumps, Recursion::plus)

fun Recursion.apply(trace: Trace): Trace? =
	when (jump) {
		is SiblingJump -> recursionOrNull.orNullApply(trace.siblingOrNull)
		is ParentJump -> recursionOrNull.orNullApply(trace.parentOrNull)
	}

fun Recursion?.orNullApply(traceOrNull: Trace?): Trace? =
	if (this == null) traceOrNull
	else traceOrNull?.let { apply(it) }
