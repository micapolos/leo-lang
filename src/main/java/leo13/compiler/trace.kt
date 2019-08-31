package leo13.compiler

import leo.base.fold
import leo.base.orIfNull
import leo13.script.Script
import leo13.script.Scriptable
import leo13.script.plus
import leo13.script.script
import leo13.type.*

data class Trace(val popOrNull: Trace?, val type: Type) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName: String get() = "trace"
	override val scriptableBody: Script
		get() =
			popOrNull?.scriptableBody.orIfNull { script() }.plus(type.scriptableLine)
}

val Type.trace get() = Trace(null, this)
fun Trace?.push(type: Type) = Trace(this, type)
fun trace(type: Type, vararg types: Type): Trace = type.trace.fold(types) { push(it) }
fun trace() = trace(type())

fun Trace.plus(line: TypeLine): Trace =
	updateType { plus(line) }

fun Trace.set(type: Type): Trace =
	popOrNull.push(type)

fun Trace.updateType(fn: Type.() -> Type): Trace =
	set(type.fn())

fun Trace.applyOrNull(recursion: Recursion): Trace? =
	if (recursion.lhsOrNull == null) this
	else popOrNull?.applyOrNull(recursion.lhsOrNull)

fun Trace.applyOrNull(thunk: TypeThunk): Trace? =
	when (thunk) {
		is TypeTypeThunk -> push(thunk.type)
		is RecursionTypeThunk -> applyOrNull(thunk.recursion)
	}

val Trace.lhsOrNull: Trace?
	get() =
		type.previousOrNull?.let { lhsType ->
			updateType { lhsType }
		}

val Trace.rhsOrNull: Trace?
	get() =
		type.rhsThunkOrNull?.let { rhsThunk ->
			applyOrNull(rhsThunk)
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
