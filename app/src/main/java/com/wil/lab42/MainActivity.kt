//Wilson Peña - 24760
//Link del video:
// https://youtu.be/9KJlbvvLzxI

package com.wil.lab42

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.wil.lab42.ui.theme.HealthyLivingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HealthyLivingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HealthyLivingApp()
                }
            }
        }
    }
}

// Data class para representar una receta
data class Recipe(
    val name: String,
    val imageUrl: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthyLivingApp() {
    // Estados para manejar los campos de texto
    var recipeName by remember { mutableStateOf("") }
    var recipeImageUrl by remember { mutableStateOf("") }

    // Lista mutable para almacenar las recetas
    val recipeList = remember { mutableStateListOf<Recipe>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título de la app
        Text(
            text = "Healthy Living App",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Campo de texto para el nombre de la receta
        OutlinedTextField(
            value = recipeName,
            onValueChange = { recipeName = it },
            label = { Text("Nombre de la receta") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            singleLine = true
        )

        // Campo de texto para la URL de la imagen
        OutlinedTextField(
            value = recipeImageUrl,
            onValueChange = { recipeImageUrl = it },
            label = { Text("URL de la imagen") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            singleLine = true
        )

        // Botón para agregar receta
        Button(
            onClick = {
                // Validación simple: no agregar si algún campo está vacío
                if (recipeName.isNotBlank() && recipeImageUrl.isNotBlank()) {
                    // Crear nueva receta y agregarla a la lista
                    val newRecipe = Recipe(
                        name = recipeName,
                        imageUrl = recipeImageUrl
                    )
                    recipeList.add(newRecipe)

                    // Limpiar los campos después de agregar
                    recipeName = ""
                    recipeImageUrl = ""
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            Text("Agregar")
        }

        // Lista de recetas usando LazyColumn
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(recipeList) { recipe ->
                RecipeCard(
                    recipe = recipe,
                    onDelete = { recipeList.remove(recipe) }
                )
            }
        }
    }
}

@Composable
fun RecipeCard(
    recipe: Recipe,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Imagen usando Coil
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(recipe.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Imagen de ${recipe.name}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Nombre de la receta
            Text(
                text = recipe.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Botón para eliminar (desafío adicional)
            Button(
                onClick = onDelete,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Eliminar")
            }
        }
    }
}