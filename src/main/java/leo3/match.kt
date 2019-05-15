package leo3

sealed class Match
data class FunctionMatch(val function: Function) : Match()
data class TemplateMatch(val template: Template) : Match()
