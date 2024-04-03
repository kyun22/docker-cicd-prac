package point.dto

class PointRequest {
    data class Charge(
        val userId: String,
        val amount: Int,
    )
}
