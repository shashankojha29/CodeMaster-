package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.ChallengeSubmission
import com.example.data.model.CareerCourseProgress
import com.example.data.model.CodeLabFileEntity
import com.example.data.model.CodeLabProjectEntity
import com.example.data.model.LessonProgress
import com.example.data.model.ProjectProgress
import com.example.data.model.QuizResult
import com.example.data.model.RoadmapProgress
import com.example.data.model.TypingResult
import com.example.data.model.UnlockedAchievement
import com.example.data.model.UserProfile

@Database(
    entities = [
        UserProfile::class,
        LessonProgress::class,
        QuizResult::class,
        ChallengeSubmission::class,
        TypingResult::class,
        RoadmapProgress::class,
        ProjectProgress::class,
        UnlockedAchievement::class,
        CareerCourseProgress::class,
        CodeLabProjectEntity::class,
        CodeLabFileEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class CodeMasterDatabase : RoomDatabase() {

    abstract fun codeMasterDao(): CodeMasterDao

    companion object {
        @Volatile
        private var INSTANCE: CodeMasterDatabase? = null

        fun getInstance(context: Context): CodeMasterDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CodeMasterDatabase::class.java,
                    "codemaster_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
