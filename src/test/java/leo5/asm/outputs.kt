package leo5.asm

class OutputCaptureState(val array: IntArray, var index: Int)

fun assertOutputs(array: IntArray, fn: (Output) -> Unit) {
	OutputCaptureState(array, 0).run {
		fn(output { int ->
			if (index == array.size) assert(false) { "output overflow" }
			else {
				assert(array[index] == int) { "output[$index]: expected ${array[index]}, was $int" }
				index++
			}
		})
		assert(index == array.size) { "size: expected ${array.size}, was $index" }
	}
}