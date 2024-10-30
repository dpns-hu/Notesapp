package com.sb.pratilipinotesapp.presentation.AddEditNote

import android.annotation.SuppressLint
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sb.pratilipinotesapp.data.model.Note
import com.sb.pratilipinotesapp.presentation.NotesList.NoteListViewModel
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "SuspiciousIndentation")
@Composable
fun AddEditNoteScreen(
    navController: NavController,
    noteTitle:String,
    noteContent:String,
    noteId:String,
    notePosition:String,
    viewModel: NoteListViewModel = hiltViewModel()
) {
    val titleState = viewModel.noteTitleState.collectAsState()
    val contentState = viewModel.noteContentState.collectAsState()
      viewModel.onTitleChanged(noteTitle)
    viewModel.onContentChanged(noteContent)
     val scaffoldState = rememberScaffoldState()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if(noteId==""){
                        viewModel.addNote(Note(titleState.value,contentState.value,-1))
                        navController.navigateUp()
                    }else{
                        viewModel.addNote(Note(titleState.value,contentState.value,notePosition.toInt(),noteId.toInt()))
                        navController.navigateUp()
                    }

                },
                backgroundColor = MaterialTheme.colors.primary,
                modifier = Modifier.padding(bottom = 64.dp)
            ) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "Save note")
            }
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 30.dp, start = 16.dp,end = 16.dp, bottom = 16.dp)
        ) {
            Text(
                text = "Add/Update Notes",
                style = MaterialTheme.typography.h5,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            // Title Heading
            Text(
                text = "Title",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            BasicTextField(
                value = titleState.value,
                onValueChange = { viewModel.onTitleChanged(it) },
                singleLine = true,
                textStyle = TextStyle(
                    color = MaterialTheme.colors.onSurface,
                    fontSize = 20.sp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.surface, shape = RoundedCornerShape(8.dp))
                    .border(1.dp, MaterialTheme.colors.primary, RoundedCornerShape(8.dp))
                    .padding(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Description Heading
            Text(
                text = "Description",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            BasicTextField(
                value = contentState.value,
                onValueChange = { viewModel.onContentChanged(it) },
                textStyle = TextStyle(
                    color = MaterialTheme.colors.onSurface,
                    fontSize = 20.sp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.surface, shape = RoundedCornerShape(8.dp))
                    .border(1.dp, MaterialTheme.colors.primary, RoundedCornerShape(8.dp))
                    .padding(12.dp)
            )
        }
    }
}

