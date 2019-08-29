package leo13.type.pattern

import leo.base.Empty
import leo.base.empty
import leo.base.fold
import leo.base.notNullIf
import leo13.script.lineTo
import leo13.script.script

fun type(empty: Empty): Type = EmptyType(empty)
fun type(link: TypeLink): Type = LinkType(link)
fun type(choice: Choice): Type = ChoiceType(choice)
fun type(arrow: TypeArrow): Type = FunctionType(arrow)

fun type(vararg lines: TypeLine): Type = type(empty).fold(lines) { plus(it) }
fun type(name: String): Type = type(name lineTo type())

infix fun String.lineTo(rhs: Type) = TypeLine(this, rhs)
fun link(lhs: Type, line: TypeLine) = TypeLink(lhs, line)

fun uncheckedChoice(lhsNode: ChoiceNode, case: Case) = Choice(lhsNode, case)

fun choiceNode(case: Case): ChoiceNode = CaseChoiceNode(case)
fun node(choice: Choice): ChoiceNode = ChoiceChoiceNode(choice)

infix fun String.caseTo(rhs: Type) = Case(this, rhs)
infix fun Type.arrowTo(rhs: Type) = TypeArrow(this, rhs)
fun arrow(lhs: Type, rhs: Type) = lhs arrowTo rhs

fun Type.plus(line: TypeLine): Type = type(link(this, line))
fun TypeLink.plus(line: TypeLine): TypeLink = link(type(this), line)

fun Choice.unsafePlus(case: Case): Choice =
	if (rhsOrNull(case.name) != null) error("duplicate" lineTo script("case" lineTo script(case.name)))
	else uncheckedChoice(node(this), case)

fun ChoiceNode.unsafePlusChoice(case: Case): Choice =
	if (rhsOrNull(case.name) != null) error("duplicate" lineTo script("case" lineTo script(case.name)))
	else uncheckedChoice(this, case)

fun Choice.rhsOrNull(name: String): Type? = case.rhsOrNull(name) ?: lhsNode.rhsOrNull(name)
fun ChoiceNode.rhsOrNull(name: String): Type? = when (this) {
	is CaseChoiceNode -> case.rhsOrNull(name)
	is ChoiceChoiceNode -> choice.rhsOrNull(name)
}

fun Case.rhsOrNull(name: String): Type? = notNullIf(this.name == name) { rhs }

fun unsafeChoice(firstCase: Case, secondCase: Case, vararg cases: Case) =
	choiceNode(firstCase).unsafePlusChoice(secondCase).fold(cases) { unsafePlus(it) }

val TypeLine.case: Case get() = name caseTo rhs