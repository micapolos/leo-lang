package leo13.type.pattern

fun Type.contains(type: Type): Boolean =
	when (this) {
		is EmptyType -> type is EmptyType
		is ChoiceType -> choice.contains(type)
		is FunctionType -> type is FunctionType && arrow == type.arrow
		is LinkType -> type is LinkType && link.contains(type.link)
	}

fun TypeLine.contains(line: TypeLine): Boolean =
	name == line.name && rhs.contains(line.rhs)

fun TypeLink.contains(link: TypeLink): Boolean =
	lhs.contains(link.lhs) && line.contains(link.line)

fun Choice.contains(type: Type): Boolean =
	when (type) {
		is EmptyType -> false
		is LinkType -> contains(type.link)
		is ChoiceType -> this == type.choice
		is FunctionType -> false
	}

fun Choice.contains(typeLink: TypeLink): Boolean =
	typeLink.lhs is EmptyType && contains(typeLink.line)

fun Choice.contains(typeLine: TypeLine): Boolean =
	case.contains(typeLine) || lhsNode.contains(typeLine)

fun ChoiceNode.contains(typeLine: TypeLine): Boolean =
	when (this) {
		is CaseChoiceNode -> case.contains(typeLine)
		is ChoiceChoiceNode -> choice.contains(typeLine)
	}

fun Case.contains(typeLine: TypeLine): Boolean =
	name == typeLine.name && rhs == typeLine.rhs

fun TypeArrow.contains(arrow: TypeArrow): Boolean =
	this == arrow
