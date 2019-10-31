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
		is DoubleExpression -> write(expression.double)
		is StringExpression -> write(expression.string)
		is NativeExpression -> write(expression.native)
		is LinkExpression -> write(expression.link)
		is ApplyExpression -> write(expression.apply)
		else -> TODO()
	}

fun Compiler.writeNull() =
	write("null") { this }

fun Compiler.write(double: Double) =
	write(token(double))

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