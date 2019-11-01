package leo13.js

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

fun Compiler.write(expression: Expression): Compiler =
	when (expression) {
		is NullExpression -> writeNull()
		is NumberExpression -> write(expression.number)
		is StringExpression -> write(expression.string)
		is NativeExpression -> write(expression.native)
		is LinkExpression -> write(expression.link)
		is ApplyExpression -> write(expression.apply)
		else -> TODO()
	}

fun Compiler.writeNull() =
	write("null") { this }

fun Compiler.write(number: Number) =
	write(token(number))

fun Compiler.write(string: String) =
	write(token(string))

fun Compiler.write(native: Native) =
	write("native") {
		write(token(native.string))
	}

fun Compiler.write(link: ExpressionLink) =
	this
		.write(link.lhs)
		.write(link.rhs)

fun Compiler.write(apply: Apply) =
	this
		.write(apply.lhs)
		.write("apply") {
			this
				.write(apply.rhs)
				.write(token(end))
		}

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
