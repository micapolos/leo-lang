package leo13.type

import leo.base.fold
import leo.base.orIfNull
import leo13.Scriptable
import leo13.script.Script
import leo13.script.plus
import leo13.script.script

data class Type(val popOrNull: Type?, val pattern: Pattern) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName: String get() = "type"
	override val scriptableBody: Script
		get() =
			popOrNull?.scriptableBody.orIfNull { script() }.plus(pattern.scriptableLine)
}

val Pattern.type get() = Type(null, this)
fun Type?.push(pattern: Pattern) = Type(this, pattern)
fun type(pattern: Pattern, vararg patterns: Pattern): Type = pattern.type.fold(patterns) { push(it) }
fun type() = type(pattern())

val Type.unsafeStaticScript
	get() =
		pattern.unsafeStaticScript

fun Type.plus(line: PatternLine): Type =
	updatePattern { plus(line) }

fun Type.set(pattern: Pattern): Type =
	popOrNull.push(pattern)

fun Type.updatePattern(fn: Pattern.() -> Pattern): Type =
	set(pattern.fn())

fun Type.applyOrNull(recursion: Recursion): Type? =
	if (recursion.lhsOrNull == null) this
	else popOrNull?.applyOrNull(recursion.lhsOrNull)

fun Type.applyOrNull(thunk: PatternRhs): Type? =
	when (thunk) {
		is TypePatternRhs -> push(thunk.pattern)
		is RecursionPatternRhs -> applyOrNull(thunk.recursion)
	}

val Type.lhsOrNull: Type?
	get() =
		pattern.previousOrNull?.let { lhsType ->
			updatePattern { lhsType }
		}

val Type.lineOrNull: Type?
	get() =
		pattern.lineOrNull?.let { type ->
			set(type)
		}

val Type.rhsOrNull: Type?
	get() =
		pattern.rhsThunkOrNull?.let { rhsThunk ->
			applyOrNull(rhsThunk)
		}

fun Type.rhsOrNull(name: String): Type? =
	pattern.rhsThunkOrNull(name)?.let { thunk ->
		applyOrNull(thunk)
	}

fun Type.accessOrNull(name: String): Type? =
	pattern.onlyLineOrNull?.let { line ->
		applyOrNull(line.rhs)?.let { trace ->
			trace.pattern.rhsThunkOrNull(name)?.let { rhsThunk ->
				updatePattern { pattern(name lineTo rhsThunk) }
			}
		}
	}

fun Type.contains(type: Type): Boolean =
	// TODO: Check for recursion!!!
	pattern.contains(type.pattern)