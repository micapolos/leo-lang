package vm3

data class Layout(val size: Int, val body: Body) {
	sealed class Body {
		object Bool : Body()
		object I32 : Body()
		object F32 : Body()
		data class Array(val itemLayout: Layout, val itemCount: Int) : Body()
		data class Struct(val fieldIndices: Map<String, Int>, val fields: List<Field>) : Body()
		data class Choice(val fieldIndices: Map<String, Int>, val fields: List<Field>) : Body()
		data class Field(val offset: Int, val layout: Layout)
	}
}
