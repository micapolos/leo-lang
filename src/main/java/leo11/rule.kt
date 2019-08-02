package leo11

data class Rule(val pattern: Pattern, val body: Body)

infix fun Pattern.ruleTo(body: Body) = Rule(this, body)
