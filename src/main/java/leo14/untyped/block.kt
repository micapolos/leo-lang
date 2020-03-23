package leo14.untyped

import leo14.Script

data class Block(val context: Context, val script: Script) {
	override fun toString() = script.toString()
}

fun block(context: Context, script: Script) = Block(context, script)