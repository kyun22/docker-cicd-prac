package kr.shlee.domain.common.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DateUtils {
    companion object {
        fun convertStringToLocalDate(dateString: String): LocalDate {
            return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }
    }
}