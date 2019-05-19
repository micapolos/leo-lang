package leo3

data class Body(val templateOrNull: Template?)

fun body(templateOrNull: Template? = null) = Body(templateOrNull)
fun Body.apply(parameter: Parameter) = templateOrNull?.apply(parameter) ?: value()