package leo12

import leo10.Dict
import sun.invoke.empty.Empty

sealed class Code
data class EmptyCode(val empty: Empty) : Code()
data class BodyCode(val body: CodeBody) : Code()

sealed class CodeBody
data class ArgumentCodeBody(val argument: Argument) : CodeBody()
data class LinkCodeBody(val link: CodeLink)

object Argument

sealed class ScriptCode
data class NameScriptCode(val name: String) : ScriptCode()
data class LinkScriptCode(val link: CodeLink) : ScriptCode()

data class CodeLink(val lhs: Code, val line: CodeLine)
data class CodeLine(val name: String, val rhs: CodeBody)

sealed class Op
//data class LhsOp(val lhs: Lhs): Op()
//data class RhsOp(val rhs: Rhs): Op()
data class SwitchOp(val switch: CodeSwitch) : Op()
//data class CallOp(val call: CodeCall): Op()

data class CodeSwitch(val dict: Dict<Code>)
//data class CodeCall(val function: Function)