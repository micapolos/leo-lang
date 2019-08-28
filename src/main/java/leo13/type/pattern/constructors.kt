package leo13.type.pattern

import leo.base.Empty
import leo.base.empty
import leo.base.fold

fun type(empty: Empty): Type = EmptyType(empty)
fun type(link: TypeLink): Type = LinkType(link)
fun type(choice: Choice): Type = ChoiceType(choice)
fun type(arrow: TypeArrow): Type = FunctionType(arrow)

fun type(vararg lines: TypeLine): Type = type(empty).fold(lines) { plus(it) }
fun type(name: String): Type = type(name lineTo type())

infix fun String.lineTo(rhs: Type) = TypeLine(this, rhs)
fun link(lhs: Type, line: TypeLine) = TypeLink(lhs, line)

fun choice(lhsNode: ChoiceNode, case: Case) = Choice(lhsNode, case)

fun choiceNode(case: Case): ChoiceNode = CaseChoiceNode(case)
fun node(choice: Choice): ChoiceNode = ChoiceChoiceNode(choice)

infix fun String.caseTo(rhs: Type) = Case(this, rhs)
infix fun Type.arrowTo(rhs: Type) = TypeArrow(this, rhs)
fun arrow(lhs: Type, rhs: Type) = lhs arrowTo rhs

fun Type.plus(line: TypeLine): Type = type(link(this, line))
fun TypeLink.plus(line: TypeLine): TypeLink = link(type(this), line)

fun Choice.plus(case: Case): Choice = choice(node(this), case)
fun ChoiceNode.plus(case: Case): ChoiceNode = node(choice(this, case))

fun choice(firstCase: Case, secondCase: Case, vararg cases: Case) =
	choice(choiceNode(firstCase), secondCase).fold(cases) { plus(it) }

val TypeLine.case: Case get() = name caseTo rhs