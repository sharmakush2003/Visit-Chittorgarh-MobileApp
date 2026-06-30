package com.example.visitchittorgarh.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// ─────────────────────────────────────────────────────────────────────────────
// Core shimmer brush — single source of truth for the animated gradient
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun rememberShimmerBrush(
    shimmerColors: List<Color> = listOf(
        Color(0xFF2A2A2A),
        Color(0xFF3D3D3D),
        Color(0xFF2A2A2A),
    )
): Brush {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1200f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )
    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim - 400f, translateAnim - 400f),
        end = Offset(translateAnim, translateAnim)
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Generic shimmer box — basic building block
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun ShimmerBox(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 8.dp,
    brush: Brush = rememberShimmerBrush()
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(brush)
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Attraction card skeleton — matches AttractionCard layout exactly
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun AttractionCardSkeleton(modifier: Modifier = Modifier) {
    val brush = rememberShimmerBrush()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1E1E1E))
    ) {
        // Image placeholder
        ShimmerBox(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            cornerRadius = 0.dp,
            brush = brush
        )
        Column(modifier = Modifier.padding(16.dp)) {
            // Category chip + rating row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ShimmerBox(
                    modifier = Modifier.width(80.dp).height(22.dp),
                    cornerRadius = 6.dp,
                    brush = brush
                )
                ShimmerBox(
                    modifier = Modifier.width(48.dp).height(22.dp),
                    cornerRadius = 6.dp,
                    brush = brush
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            // Title
            ShimmerBox(
                modifier = Modifier.fillMaxWidth(0.65f).height(20.dp),
                brush = brush
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Description line 1
            ShimmerBox(
                modifier = Modifier.fillMaxWidth().height(13.dp),
                brush = brush
            )
            Spacer(modifier = Modifier.height(6.dp))
            // Description line 2 (shorter)
            ShimmerBox(
                modifier = Modifier.fillMaxWidth(0.75f).height(13.dp),
                brush = brush
            )
            Spacer(modifier = Modifier.height(18.dp))
            // Bottom action row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ShimmerBox(
                    modifier = Modifier.width(100.dp).height(14.dp),
                    brush = brush
                )
                ShimmerBox(
                    modifier = Modifier.width(100.dp).height(14.dp),
                    brush = brush
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Hero banner skeleton — for the top parallax image area
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun HeroBannerSkeleton(height: Dp = 280.dp) {
    val brush = rememberShimmerBrush()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(brush)
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Package card skeleton — for PackagesScreen
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun PackageCardSkeleton(modifier: Modifier = Modifier) {
    val brush = rememberShimmerBrush()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1E1E1E))
    ) {
        ShimmerBox(
            modifier = Modifier.fillMaxWidth().height(160.dp),
            cornerRadius = 0.dp,
            brush = brush
        )
        Column(modifier = Modifier.padding(16.dp)) {
            ShimmerBox(modifier = Modifier.fillMaxWidth(0.7f).height(18.dp), brush = brush)
            Spacer(modifier = Modifier.height(8.dp))
            ShimmerBox(modifier = Modifier.fillMaxWidth().height(13.dp), brush = brush)
            Spacer(modifier = Modifier.height(5.dp))
            ShimmerBox(modifier = Modifier.fillMaxWidth(0.8f).height(13.dp), brush = brush)
            Spacer(modifier = Modifier.height(14.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                ShimmerBox(modifier = Modifier.width(80.dp).height(14.dp), brush = brush)
                ShimmerBox(modifier = Modifier.width(100.dp).height(32.dp), cornerRadius = 20.dp, brush = brush)
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Full HomeScreen loading shimmer state
// ─────────────────────────────────────────────────────────────────────────────
@Composable
fun HomeScreenSkeleton() {
    Column(modifier = Modifier.fillMaxSize()) {
        HeroBannerSkeleton(height = 280.dp)
        repeat(3) {
            AttractionCardSkeleton()
        }
    }
}
