define: circle has: radius: number

define: rectangle has
	width: number
	height: number

define: shape has
	either: circle
	either: rectangle

define: circle area gives
	pi number
	times: given area circle radius number
	times: given area circle radius number

define: rectangle area gives
	given area rectangle width number
	times: given area rectangle height number
