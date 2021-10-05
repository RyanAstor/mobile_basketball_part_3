package com.bignerdranch.android.project_1

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity (tableName = "table_game")
data class Game(@PrimaryKey val id: UUID = UUID.randomUUID(),
                var teamAName: String = "",
                var teamBName: String = "",
                var teamAScore: Int = 0,
                var teamBScore: Int = 0,
                var date: Date = Date()) {

    val photoFileNameA
        get() = "IMG_A_$id.jpg"
    val photoFileNameB
        get() = "IMG_B_$id.jpg"
}