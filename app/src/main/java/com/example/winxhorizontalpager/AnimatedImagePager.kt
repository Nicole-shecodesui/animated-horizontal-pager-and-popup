package com.example.winxhorizontalpager

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlin.math.absoluteValue

// Data class representing a Winx character with title, color, image, and description
data class PagerItem(
    val title: String,
    val backgroundColor: Color,
    val imageRes: Int,
    val description: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedImagePager() {
    // List of characters to show in the pager
    val pages = listOf(
        PagerItem(
            "Aisha", Color(0xFFE796CA), R.drawable.aisha,
            "Aisha is the Fairy of Waves, known for her athleticism and strong will. She symbolizes fluidity and strength."
        ),
        PagerItem(
            "Stella", Color(0xFFA284DE), R.drawable.stella,
            "Stella is the Fairy of the Shining Sun, a fashionista who symbolizes radiance and confidence."
        ),
        PagerItem(
            "Flora", Color(0xFFC97474), R.drawable.flora,
            "Flora is the Fairy of Nature, gentle and harmonious. She symbolizes growth and compassion."
        ),
        PagerItem(
            "Tecna", Color(0xFF7FC576), R.drawable.tecna,
            "Tecna is the Fairy of Technology, smart and logical. She symbolizes innovation and intelligence."
        ),
        PagerItem(
            "Musa", Color(0xFFD97D4C), R.drawable.musa,
            "Musa is the Fairy of Music, emotional and expressive. She symbolizes creativity and rhythm."
        ),
        PagerItem(
            "Bloom", Color(0xFF4E7DC2), R.drawable.bloom,
            "Bloom is the Fairy of the Dragon Flame, the leader. She symbolizes inner strength and destiny."
        )
    )

    // State to track which character is currently selected
    var selectedCharacter by remember { mutableStateOf(pages[0]) }

    // Pager state to control and observe the horizontal pager
    val pagerState = rememberPagerState(pageCount = { pages.size })

    // State to control visibility of the popup dialog
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(selectedCharacter.backgroundColor) // Background color changes with character
            .padding(30.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(30.dp))

            // Title text
            Text(
                text = "Pick Your Winx",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )

            Spacer(Modifier.height(16.dp))

            // Horizontal pager to swipe through characters
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color.Blue.copy(alpha = 0.18f)),
                contentPadding = PaddingValues(horizontal = 20.dp),
                pageSpacing = 16.dp
            ) { page ->
                val pageOffset =
                    (pagerState.currentPage - page + pagerState.currentPageOffsetFraction).absoluteValue
                val item = pages[page]

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .graphicsLayer {
                            // Scale animation makes center item larger
                            val scale = 1f - pageOffset * 0.9f
                            scaleX = scale
                            scaleY = scale
                        }
                        .fillMaxHeight()
                ) {
                    Image(
                        painter = painterResource(id = item.imageRes),
                        contentDescription = item.title,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // Dot indicator to show current position in pager
            AnimatedDotIndicator(
                totalDots = pages.size,
                selectedIndex = pagerState.currentPage,
                modifier = Modifier.padding(8.dp),
                selectedColor = Color.White,
                unselectedColor = Color.White.copy(alpha = 0.4f)
            )

            Spacer(Modifier.height(20.dp))

            // Button to select the current character
            Button(
                onClick = {
                    selectedCharacter = pages[pagerState.currentPage]
                    showDialog = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text(
                    "Select ${pages[pagerState.currentPage].title}",
                    color = selectedCharacter.backgroundColor
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }

    // Dialog popup showing character details
    if (showDialog) {
        // Animation states
        var startAnimation by remember { mutableStateOf(false) }

        // Animate rotation from 90 to 360 degrees
        val rotation by animateFloatAsState(
            targetValue = if (startAnimation) 360f else 90f,
            animationSpec = tween(durationMillis = 400)
        )

        // Animate scale from 0.01 to 1
        val scale by animateFloatAsState(
            targetValue = if (startAnimation) 1f else 0.01f,
            animationSpec = tween(durationMillis = 600)
        )

        //fade in smoothly
        val alpha by animateFloatAsState(
            targetValue = if (startAnimation) 1f else 0f,
            animationSpec = tween(durationMillis = 500)
        )

        // Trigger animation on first composition
        LaunchedEffect(Unit) {
            startAnimation = true
        }

        Dialog(onDismissRequest = { showDialog = false }) {
            Box(
                modifier = Modifier
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        rotationZ = rotation,
                        alpha = alpha
                    )
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = selectedCharacter.backgroundColor,
                    tonalElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = selectedCharacter.title,
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White
                        )

                        Spacer(Modifier.height(16.dp))

                        Image(
                            painter = painterResource(id = selectedCharacter.imageRes),
                            contentDescription = selectedCharacter.title,
                            modifier = Modifier
                                .height(200.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                        )

                        Spacer(Modifier.height(16.dp))

                        Text(
                            text = selectedCharacter.description,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(24.dp))

                        Button(
                            onClick = { showDialog = false },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            modifier = Modifier.fillMaxWidth(0.5f)
                        ) {
                            Text("Back", color = selectedCharacter.backgroundColor)
                        }
                    }
                }
            }
        }
    }

}

// Composable showing animated dots indicating pager position
@Composable
fun AnimatedDotIndicator(
    totalDots: Int,
    selectedIndex: Int,
    modifier: Modifier = Modifier,
    selectedColor: Color = Color.Magenta,
    unselectedColor: Color = Color.LightGray
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        for (i in 0 until totalDots) {
            // Dot grows in size when selected
            val size by animateDpAsState(targetValue = if (i == selectedIndex) 10.dp else 6.dp)
            Surface(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(size)
                    .clip(CircleShape),
                color = if (i == selectedIndex) selectedColor else unselectedColor,
                shape = CircleShape
            ) {}
        }
    }
}
