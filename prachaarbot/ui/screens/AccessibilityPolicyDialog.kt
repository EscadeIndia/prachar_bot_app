package com.prachaarbot.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.prachaarbot.ui.theme.Monstserrat

@Composable
fun AccessibilityPolicyDialog(callback: (result: Boolean) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            shape = RoundedCornerShape(2.dp),
            elevation = 30.dp, backgroundColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color.White)
            ) {
                Text(
                    text = "Accessibility Permission Disclosure:",
                    style = MaterialTheme.typography.h6
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Why need Accessibility permission (Accessibility service)?",
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    fontFamily = Monstserrat,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "The Unique Promotion App is NOT affiliated with WhatsApp.",
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray,
                    fontFamily = Monstserrat,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "The Accessibility service is used to send WhatsApp messages automatically. And it will be ONLY used for this purpose.",
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray,
                    fontFamily = Monstserrat,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "The Accessibility service does NOT collect or share any information from you during your use of or access to the service.",
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray,
                    fontFamily = Monstserrat,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "We NEVER SHARE your personal information.",
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    fontFamily = Monstserrat,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Purpose to use, Accessibility service is mentioned above in very simple English language. Still your permission is needed once again to continue further.",
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    fontFamily = Monstserrat,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Do you agree?",
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    fontFamily = Monstserrat,
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        modifier = Modifier
                            .clickable {
                                callback.invoke(false)
                            },
                        text = "DENY", fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.primary,
                        fontFamily = Monstserrat
                    )
                    Spacer(modifier = Modifier.width(15.dp))
                    Text(
                        modifier = Modifier
                            .clickable {
                                callback.invoke(true)
                            },
                        text =
                        "AGREE",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.primary,
                        fontFamily = Monstserrat,
                    )

                }
            }
        }
    }
}
