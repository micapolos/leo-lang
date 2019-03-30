package leo32

data class ScriptWriter(
	val appendable: Appendable)

val Appendable.scriptWriter
	get() =
		ScriptWriter(this)

fun ScriptWriter.writeField(key: String, value: ScriptWriter.() -> ScriptWriter = { this }): ScriptWriter =
	appendable
		.append(key)
		.append(" ")
		.scriptWriter.value().appendable
		.append(" ")
		.scriptWriter

fun ScriptWriter.writeField(key: String, value: String): ScriptWriter =
	writeField(key) { writeField(value) }
