package lambda

class Application(val left: Term, val right: Term) {
	override fun toString() = code
	override fun equals(other: Any?) = other is Application && eq(other)
}

fun application(left: Term, right: Term) = Application(left, right)
fun Application.eq(application: Application) = left.eq(application.left) && right.eq(application.right)
val Application.reduce get() = left.reduce.invoke(right.reduce)
val Application.code get() = "${left.code}.apply(${right.code})"
