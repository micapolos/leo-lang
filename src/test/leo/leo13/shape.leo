define
	circle has: radius: number

	rectangle has
		width: number
		height: number

	shape has
		either: circle
		either: rectangle

	circle area gives: area
		pi number
		times: given circle radius number
		times: given circle radius number

	rectangle area gives: area
		given rectangle width number
		times: given rectangle height number

	shape area gives
		given shape switch
			circle: switched circle area
			rectangle: switched rectangle area
