package leo25

import kotlinx.collections.immutable.PersistentMap

data class Word(val string: String)

sealed class Value
object EmptyValue : Value()
data class ThingValue(val thing: Thing) : Value()

sealed class Thing
data class WordThing(val word: Word) : Thing()
data class StringThing(val string: String) : Thing()
data class LinkThing(val link: Link) : Thing()
data class FunctionThing(val function: Function) : Thing()

data class Link(val value: Value, val line: Line)
data class Line(val word: Word, val thing: Thing)
data class Function(val context: Context, val body: Body)
data class Body(val value: Value)

data class Context(val tokenToResolutionMap: PersistentMap<Token, Resolution>)

sealed class Token
data class BeginToken(val begin: Begin) : Token()
data class EndToken(val end: End) : Token()

data class Begin(val word: Word) : Token()

sealed class End
object EmptyEnd : End()
object AnythingEnd : End()

sealed class Resolution
data class ContextResolution(val context: Context) : Resolution()
data class BindingResolution(val binding: Binding) : Resolution()

sealed class Binding
data class ValueBinding(val value: Value) : Binding()
data class FunctionBinding(val function: Function) : Binding()