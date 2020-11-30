package leo23.term.type

val nilType: Type get() = NilType
val booleanType: Type get() = BooleanType
val textType: Type get() = TextType
val numberType: Type get() = NumberType
fun type(vararg types: Type): Type = TupleType(listOf(*types))
fun choiceType(vararg types: Type): Type = ChoiceType(listOf(*types))
infix fun List<Type>.does(rhs: Type): Type = ArrowType(this, rhs, false)
infix fun List<Type>.doesRecursively(rhs: Type): Type = ArrowType(this, rhs, true)
