package leo13

data class TypeArrow(val from: Type, val to: Type)

infix fun Type.arrowTo(type: Type) = TypeArrow(this, type)