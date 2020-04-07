package leo14.untyped.typed

//val Script.type: Type get() =
//	when (this) {
//		is UnitScript -> emptyType
//		is LinkScript -> link.typeLink.type
//	}
//
//val ScriptLink.typeLink: TypeLink get() =
//	lhs.type linkTo line.typeLine
//
//val ScriptLine.choice: Choice get() =
//	when (this) {
//		is LiteralScriptLine -> static.typeLine.choice
//		is FieldScriptLine -> field.choice
//	}
//
//val ScriptField.choice: Choice get() =
//	if (string == eitherName) rhs.choice
//	else typeField.line.choice
//
//val Script.choice: Choice get() =
//	when (this) {
//		is UnitScript -> emptyChoice
//		is LinkScript -> link.choiceLink.choice
//	}
//
//val ScriptLink.choiceLink: ChoiceLink get() =
//	lhs.choice linkTo line.typeLine
//
//val ScriptLine.typeLine: TypeLine get() =
//	when (this) {
//		is FieldScriptLine -> field.typeLine
//		is LiteralScriptLine -> static.typeLine
//	}
//
//val ScriptField.typeLine: TypeLine get() =
//	when (string) {
//		textName -> notNullIf(rhs.isEmpty) { textTypeLine }
//		numberName -> notNullIf(rhs.isEmpty) { numberTypeLine }
//		nativeName -> notNullIf(rhs.isEmpty) { nativeTypeLine }
//		recurseName -> notNullIf(rhs.isEmpty) { resurseTypeLine }
//		staticName -> rhs.linkOrNull?.onlyLineOrNull?.static?.typeLine
//		eitherName -> rhs.choice.line
//		recursiveName -> rhs.linkOrNull?.onlyLineOrNull?.typeLine?.recursive?.line
//		else -> null
//	} ?: typeField.line
//
//val ScriptField.typeField: TypeField get() =
//	string fieldTo rhs.type