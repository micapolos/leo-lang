package leo13.base

import leo13.Scriptable
import leo13.errorName
import leo13.metaName
import leo13.script.Script
import leo13.script.script

sealed class Result<out V : Scriptable> : Scriptable {
	override val scriptableName
		get() = when (this) {
			is OkResult ->
				if (value.scriptableName == errorName) metaName
				else value.scriptableName
			is ErrorResult -> errorName
		}

	override val scriptableBody
		get() = when (this) {
			is OkResult ->
				if (value.scriptableName == errorName) script(value.scriptableLine)
				else value.scriptableBody
			is ErrorResult -> script
		}
}

data class OkResult<V : Scriptable>(val value: V) : Result<V>()
data class ErrorResult<V : Scriptable>(val script: Script) : Result<V>()

fun <V : Scriptable> ok(value: V): Result<V> = OkResult(value)
fun <V : Scriptable> error(script: Script): Result<V> = ErrorResult(script)
