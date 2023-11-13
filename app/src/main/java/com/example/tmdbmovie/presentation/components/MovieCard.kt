package com.example.tmdbmovie.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.InspectableModifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.tmdbmovie.data.Movie

@Composable
fun MovieCard(
    modifier: Modifier = Modifier,
    movie: Movie,
    onClickedMovieDetail: (id: String,isFavorite: Boolean) -> Unit,
    onUpdateFavorite:(id: String) -> Unit
) {
    Column(
       modifier = modifier
    ) {
        AsyncImage(
            modifier = Modifier
                .padding(4.dp)
                .width(150.dp)
                .height(194.dp)
                .clickable {
                    onClickedMovieDetail(movie.id,movie.isFavorite)
                }
            ,
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://image.tmdb.org/t/p/original${movie.posterPath}")
                .build()
            ,
            contentDescription = movie.title,
            contentScale = ContentScale.Crop
        )



        Text(
            text = movie.title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp),
            fontSize = 16.sp,
        )


        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = movie.releaseDate,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
            IconButton(
                modifier = Modifier,
                onClick = {
                 onUpdateFavorite(movie.id)
                }
            ) {

                Icon(
                    imageVector = if(movie.isFavorite)Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "FavoriteIcon"
                )
            }

        }
    }

}