package leo13.js.compiler

interface Compiler {
	fun write(token: Token): Compiler
}

val eofCompiler = object : Compiler {
	override fun write(token: Token) = error("eof")
}

fun Compiler.write(string: String, writeRhs: Compiler.() -> Compiler) =
	this
		.write(token(begin(string)))
		.writeRhs()
		.write(token(end))

fun Compiler.writeNull() =
	write("null") { this }

fun Compiler.write(number: Number) =
	write(token(number))

fun Compiler.write(string: String) =
	write(token(string))

fun Compiler.write(script: Script): Compiler =
	when (script) {
		is UnitScript -> this
		is LinkScript -> write(script.link)
	}

fun Compiler.write(link: ScriptLink) =
	write(link.lhs).write(link.line)

fun Compiler.write(line: ScriptLine) =
	when (line) {
		is StringScriptLine -> write(line.string)
		is NumberScriptLine -> write(line.number)
		is FieldScriptLine -> write(line.field)
	}

fun Compiler.write(field: ScriptField) =
	write(field.string) {
		write(field.rhs)
	}
