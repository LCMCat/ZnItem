package tech.ccat.znitem.util.music

object Interpolator {
    
    fun linearInterpolate(x: DoubleArray, y: DoubleArray, xi: DoubleArray): DoubleArray {
        require(x.size == y.size) { "X and Y must be the same length" }
        require(x.size > 1) { "X must contain more than one value" }
        
        val segments = calculateSegments(x, y)
        return interpolateValues(xi, x, y, segments)
    }
    
    private data class Segment(
        val slope: Double,
        val intercept: Double
    )
    
    private fun calculateSegments(x: DoubleArray, y: DoubleArray): List<Segment> {
        return (0 until x.size - 1).map { i ->
            val deltaX = x[i + 1] - x[i]
            require(deltaX > 0) { "X must be sorted and monotonic" }
            
            val deltaY = y[i + 1] - y[i]
            val slope = deltaY / deltaX
            val intercept = y[i] - x[i] * slope
            
            Segment(slope, intercept)
        }
    }
    
    private fun interpolateValues(
        xi: DoubleArray,
        x: DoubleArray,
        y: DoubleArray,
        segments: List<Segment>
    ): DoubleArray {
        return DoubleArray(xi.size) { i ->
            when {
                xi[i] > x.last() || xi[i] < x.first() -> Double.NaN
                else -> {
                    val location = x.binarySearch(xi[i])
                    if (location < -1) {
                        val segmentIndex = -location - 2
                        segments[segmentIndex].let { it.slope * xi[i] + it.intercept }
                    } else {
                        y[location]
                    }
                }
            }
        }
    }
    
    fun linearInterpolate(xy: DoubleArray, x: Double): Double {
        require(xy.size % 2 == 0) { "XY must be divisible by two" }
        
        val xValues = DoubleArray(xy.size / 2) { i -> xy[i * 2] }
        val yValues = DoubleArray(xy.size / 2) { i -> xy[i * 2 + 1] }
        
        return linearInterpolate(xValues, yValues, doubleArrayOf(x))[0]
    }
}
