package leo

import leo.base.fail

object Value

val Value.reflect
	get() =
		valueWord fieldTo term(this)

fun <R> R.foldBytes(script: Term<Value>, fn: R.(Byte) -> R): R =
	foldBytes(script, { fail }, fn)
