package leo13.type

import leo.base.fold
import leo.base.orIfNull
import leo13.script.Script
import leo13.script.Scriptable
import leo13.script.plus
import leo13.script.script

data class Trace(val lhsOrNull: Trace?, val type: Type) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName: String get() = "trace"
	override val scriptableBody: Script
		get() =
			lhsOrNull?.scriptableBody.orIfNull { script() }.plus(type.scriptableLine)
}

val Type.trace get() = Trace(null, this)
fun Trace?.push(type: Type) = Trace(this, type)
fun trace(type: Type, vararg types: Type): Trace = type.trace.fold(types) { push(it) }

fun Trace.plus(line: TypeLine): Trace =
	updateType { plus(line) }

fun Trace.updateType(fn: Type.() -> Type): Trace =
	lhsOrNull.push(type.fn())

fun Trace.applyOrNull(recursion: Recursion): Trace? =
	lhsOrNull?.let { lhs ->
		if (recursion.lhsOrNull == null) lhs
		else lhs.applyOrNull(recursion.lhsOrNull)
	}

fun Trace.applyOrNull(thunk: TypeThunk): Trace? =
	when (thunk) {
		is TypeTypeThunk -> push(thunk.type)
		is RecursionTypeThunk -> applyOrNull(thunk.recursion)
	}

fun Trace.rhsOrNull(name: String): Trace? =
	type.rhsThunkOrNull(name)?.let { thunk ->
		applyOrNull(thunk)
	}

fun Trace.accessOrNull(name: String): Trace? =
	type.onlyLineOrNull?.let { line ->
		applyOrNull(line.rhs)?.let { trace ->
			trace.type.rhsThunkOrNull(name)?.let { rhsThunk ->
				updateType { type(name lineTo rhsThunk) }
			}
		}
	}
