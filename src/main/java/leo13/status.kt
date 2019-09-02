package leo13

import leo13.script.Script
import leo13.script.lineTo
import leo13.script.script

sealed class Status : LeoObject() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "status"
	override val scriptableBody get() = script(statusScriptableLine)
	abstract val statusScriptableName: String
	abstract val statusScriptableBody: Script
	val statusScriptableLine get() = statusScriptableName lineTo statusScriptableBody
}

data class OkStatus(val ok: Ok) : Status() {
	override fun toString() = scriptableLine.toString()
	override val statusScriptableName get() = ok.scriptableName
	override val statusScriptableBody get() = ok.scriptableBody
}

data class ErrorStatus(val error: ScriptError) : Status() {
	override fun toString() = scriptableLine.toString()
	override val statusScriptableName get() = error.scriptableName
	override val statusScriptableBody get() = error.scriptableBody
}

fun status(ok: Ok): Status = OkStatus(ok)
fun status(error: ScriptError): Status = ErrorStatus(error)

fun <V> V.updateIfOk(status: Status, fn: V.() -> V): V =
	when (status) {
		is OkStatus -> fn()
		is ErrorStatus -> this
	}

fun <V> V.failIfError(status: Status): V =
	when (status) {
		is OkStatus -> this
		is ErrorStatus -> fail(status.error.script)
	}
