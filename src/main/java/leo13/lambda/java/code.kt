package leo13.lambda.java

import leo13.base.linesString
import leo13.lambda.*

data class Code(val string: String)

fun code(string: String) = Code(string)

val JavaExpr.mainCode get() = code.mainCode

val String.mainCode
	get() =
		linesString(
			"import java.util.function.Function;",
			"@SuppressWarnings(\"ALL\")",
			"public final class Main {",
			"static Object fn(Function<Object, Object> fn) { return fn; }",
			"static Object apply(Object fn, Object arg) { return ((Function<Object, Object>) fn).apply(arg); }",
			"static void run(Object arg) { return; }",
			"static Object object(Runnable runnable) { runnable.run(); return null; }",
			"public static void main(String[] args) {",
			"run($this);",
			"}",
			"}"
		)

val JavaExpr.code get() = code(gen)
val JavaExpr.printCode get() = "object(() -> System.out.print($code))"

fun JavaExpr.code(gen: Gen): String =
	when (this) {
		is ValueExpr -> value.code.string
		is ArrowExpr -> arrow.code(gen)
		is LhsExpr -> lhs.code(gen)
		is RhsExpr -> rhs.code(gen)
		is FnExpr -> fn.code(gen)
		is ApExpr -> ap.code(gen)
		is ArgExpr -> arg.code(gen)
	}

fun JavaArrow.code(gen: Gen) = "new Object[] {${lhs.code(gen)}, ${rhs.code(gen)}}"
fun JavaLhs.code(gen: Gen) = "(${value.code(gen)})[0]"
fun JavaRhs.code(gen: Gen) = "(${value.code(gen)})[1]"
fun JavaFn.code(gen: Gen) = "fn(${paramCode(gen)} -> ${gen.inc { body.code(it) }})"
fun JavaAp.code(gen: Gen) = "apply(${lhs.code(gen)}, ${rhs.code(gen)})"
fun JavaArg.code(gen: Gen) = index(gen).varCode

val Int.varCode
	get() =
		leo.base.failIfOr(this < 0) { "v$this" }

fun paramCode(gen: Gen) =
	gen.depth.varCode

val arg = arg<Java>()
