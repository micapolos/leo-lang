package leo13

data class ValueOption(val valueOrNull: Value?) {
	override fun toString() = sentenceLine.toString()
}

fun valueOption(valueOrNull: Value? = null) = ValueOption(valueOrNull)
fun option(value: Value) = valueOption(value)

fun ValueOption.plus(line: ValueLine): ValueOption =
	option(valueOrNull?.plus(line) ?: value(line))

val ValueOption.sentenceLine: SentenceLine
	get() =
		scriptWord lineTo (valueOrNull?.sentenceLine?.let { sentence(it) } ?: sentence(noneWord))