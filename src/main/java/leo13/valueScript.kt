package leo13

data class ValueScript(val valueOrNull: Value?)

fun valueScript(valueOrNull: Value? = null) = ValueScript(valueOrNull)
fun script(value: Value) = valueScript(value)

fun ValueScript.plus(line: ValueLine): ValueScript =
	script(valueOrNull?.plus(line) ?: value(line))

val ValueScript.sentenceLine: SentenceLine
	get() =
		scriptWord lineTo (valueOrNull?.sentenceLine?.let { sentence(it) } ?: sentence(valueWord))