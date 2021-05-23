package leo25

data class Definition(val pattern: Pattern, val binding: Binding)

fun definition(pattern: Pattern, binding: Binding) = Definition(pattern, binding)
