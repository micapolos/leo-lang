package leo13.untyped

import leo13.script.*

data class Body(val context: Context, val script: Script) {
	override fun toString() = super.toString()
}

fun body(context: Context, script: Script) = Body(context, script)

const val bodyName = "body"

val bodyReader: Reader<Body>
	get() =
	reader(bodyName, contextReader, scriptReader, ::Body)

val bodyWriter: Writer<Body>
	get() =
	writer(
		bodyName,
		field(contextWriter) { context },
		field(scriptWriter) { script })
