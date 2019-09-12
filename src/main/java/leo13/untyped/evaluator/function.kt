package leo13.untyped.evaluator

import leo13.script.Script
import leo13.untyped.functionName

data class Function(val script: Script) {
	override fun toString() = functionName
}
