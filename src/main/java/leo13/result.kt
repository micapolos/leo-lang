package leo13

import leo13.script.Scriptable
import leo13.script.script

sealed class Result<out Success : Scriptable, out Failure : Scriptable> : Scriptable() {
	override fun toString() = scriptableLine.toString()

	override val scriptableName
		get() = when (this) {
			is SuccessResult -> "success"
			is FailureResult -> "failure"
		}

	override val scriptableBody
		get() = script(when (this) {
			is SuccessResult -> success.scriptableLine
			is FailureResult -> failure.scriptableLine
		})
}

data class SuccessResult<Success : Scriptable, Failure : Scriptable>(
	val success: Success) : Result<Success, Failure>() {
	override fun toString() = super.toString()
}

data class FailureResult<Success : Scriptable, Failure : Scriptable>(
	val failure: Failure) : Result<Success, Failure>() {
	override fun toString() = super.toString()
}

fun <Success : Scriptable, Failure : Scriptable> success(success: Success): Result<Success, Failure> =
	SuccessResult(success)

fun <Success : Scriptable, Failure : Scriptable> failure(failure: Failure): Result<Success, Failure> =
	FailureResult(failure)

val <Success : Scriptable, Failure : Scriptable> Result<Success, Failure>.orThrow: Success
	get() = when (this) {
		is SuccessResult -> success
		is FailureResult -> throw RuntimeException(failure.toString())
	}
