package vm3

import leo.base.assertEqualTo
import leo.base.assertNotNull
import vm3.dsl.data.array
import vm3.dsl.data.f32
import vm3.dsl.data.i32
import vm3.dsl.data.struct
import vm3.dsl.type.f32
import vm3.dsl.type.get
import vm3.dsl.type.i32
import vm3.dsl.type.struct
import vm3.dsl.value.fn
import vm3.dsl.value.get
import vm3.dsl.value.inc
import vm3.dsl.value.plus
import kotlin.test.Test
import vm3.dsl.value.i32 as i32v

class ExecutorTest {
	@Test
	fun compile() {
		i32.fn { this }.executor.assertNotNull
	}

	@Test
	fun bypass() {
		i32.fn { this }.executor.run {
			execute(123.i32).assertEqualTo(123.i32)
			execute(65536.i32).assertEqualTo(65536.i32)
		}
	}

	@Test
	fun i32() {
		i32.fn { inc + inc.inc }.executor.run {
			execute(10.i32).assertEqualTo(23.i32)
		}
	}

	@Test
	fun f32() {
		f32.fn { this + this }.executor.run {
			execute(12.3f.f32).assertEqualTo(24.6f.f32)
		}
	}

	@Test
	fun arr() {
		f32[2]
			.fn { this }
			.executor
			.execute(array(10f.f32, 20f.f32))
			.assertEqualTo(array(10f.f32, 20f.f32))
	}

	@Test
	fun arrAt0() {
		f32[3]
			.fn { this[0.i32v] }
			.executor
			.execute(array(10f.f32, 20f.f32))
			.assertEqualTo(10f.f32)
	}

	@Test
	fun arrAt1() {
		f32[3]
			.fn { this[1.i32v] }
			.executor
			.execute(array(10f.f32, 20f.f32))
			.assertEqualTo(20f.f32)
	}

	@Test
	fun struct() {
		struct("x" to f32, "y" to f32).fn { this }
			.executor
			.execute(struct("x" to 10f.f32, "y" to 20f.f32))
			.assertEqualTo(struct("x" to 10f.f32, "y" to 20f.f32))
	}

	@Test
	fun structGet() {
		struct("x" to f32, "y" to f32).fn { this["y"] }
			.executor
			.apply { dump }
			.execute(struct("x" to 10f.f32, "y" to 20f.f32))
			.assertEqualTo(20f.f32)
	}

	@Test
	fun arrayOfArraysGet() {
		f32[2][3].fn { this[1.i32v][1.i32v] }
			.executor
			.execute(
				array(
					array(10f.f32, 20f.f32),
					array(30f.f32, 40f.f32),
					array(50f.f32, 60f.f32)))
			.assertEqualTo(40f.f32)
	}

	@Test
	fun arrayOfStructsGet() {
		struct("x" to f32, "y" to f32)[3].fn { this[1.i32v]["y"] }
			.executor
			.execute(
				array(
					struct("x" to 10f.f32, "y" to 20f.f32),
					struct("x" to 30f.f32, "y" to 40f.f32),
					struct("x" to 50f.f32, "y" to 60f.f32)))
			.assertEqualTo(40f.f32)
	}

	@Test
	fun structOfStructsGet() {
		struct(
			"first" to struct("x" to f32, "y" to f32),
			"second" to struct("z" to f32, "w" to f32)).fn { this["second"]["w"] }
			.executor
			.execute(
				struct(
					"first" to struct("x" to 10f.f32, "y" to 20f.f32),
					"second" to struct("z" to 30f.f32, "w" to 40f.f32)))
			.assertEqualTo(40f.f32)
	}

	@Test
	fun structOfArraysGet() {
		struct(
			"first" to f32[2],
			"second" to f32[2])
			.fn { this["second"][1.i32v] }
			.executor
			.execute(
				struct(
					"first" to array(10f.f32, 20f.f32),
					"second" to array(30f.f32, 40f.f32)))
			.assertEqualTo(40f.f32)
	}
}