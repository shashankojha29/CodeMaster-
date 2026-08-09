package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SecondaryCyan
import com.example.ui.viewmodel.SearchResults

@Composable
fun SearchScreen(
    searchQuery: String,
    searchResults: SearchResults,
    onQueryChange: (String) -> Unit,
    onSelectLanguage: (String) -> Unit,
    onSelectLesson: (String) -> Unit,
    onSelectChallenge: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.testTag("search_back_button")
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }

            OutlinedTextField(
                value = searchQuery,
                onValueChange = onQueryChange,
                placeholder = { Text("Search lessons, challenges, languages...") },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search") },
                modifier = Modifier
                    .weight(1f)
                    .testTag("global_search_input_field"),
                shape = RoundedCornerShape(24.dp),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (searchQuery.isBlank()) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Type above to search across Python, C++, SQL, Quizzes, & Roadmaps!", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (searchResults.languages.isNotEmpty()) {
                    item { Text("Languages", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary) }
                    items(searchResults.languages) { lang ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectLanguage(lang.id) },
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text(lang.iconRes, fontSize = 20.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(lang.name, fontWeight = FontWeight.Bold)
                                    Text(lang.description, fontSize = 11.sp, maxLines = 1)
                                }
                            }
                        }
                    }
                }

                if (searchResults.lessons.isNotEmpty()) {
                    item { Text("Lessons", fontWeight = FontWeight.Bold, color = SecondaryCyan) }
                    items(searchResults.lessons) { lesson ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectLesson(lesson.id) },
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(lesson.title, fontWeight = FontWeight.Bold)
                                Text(lesson.explanation, fontSize = 11.sp, maxLines = 1)
                            }
                        }
                    }
                }

                if (searchResults.challenges.isNotEmpty()) {
                    item { Text("Coding Challenges", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.tertiary) }
                    items(searchResults.challenges) { challenge ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectChallenge(challenge.id) },
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(challenge.title, fontWeight = FontWeight.Bold)
                                Text(challenge.problemStatement, fontSize = 11.sp, maxLines = 1)
                            }
                        }
                    }
                }
            }
        }
    }
}
