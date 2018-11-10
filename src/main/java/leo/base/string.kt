package leo.base

val String.withoutWhitespaces
  get() =
    replace("\\s".toRegex(), "")

fun appendableString(fn: (Appendable) -> Unit): String {
  val stringBuilder = StringBuilder()
  fn(stringBuilder)
  return stringBuilder.toString()
}

fun Appendable.appendString(value: Any?) =
  append(value.string)
