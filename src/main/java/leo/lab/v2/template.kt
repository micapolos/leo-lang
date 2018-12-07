package leo.lab.v2

data class Template(
	val script: Script)

fun template(script: Script): Template =
	Template(script)