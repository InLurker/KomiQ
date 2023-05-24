
import android.icu.text.CaseMap.Title
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen() {
    TopAppBar(
        title = {
            Text(text = "Library")
        },
        actions = {
            IconButton(onClick = {

            }) {
                Icon(
                    imageVector = Icons.Outlined.History,
                    contentDescription = "History"
                )
            }
            IconButton(onClick = {

            }) {
                Icon(
                    imageVector = Icons.Outlined.MoreVert,
                    contentDescription = "History"
                )
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun LibraryPreview() {
    LibraryScreen()
}
