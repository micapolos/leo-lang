package leo32.runtime.v2

import leo.base.appendableString

data class Code(val script: Script) {
	override fun toString() = appendableString { it.append(script) }
}

val Script.code
	get() =
		Code(this)