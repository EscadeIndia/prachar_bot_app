package com.prachaarbot.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.prachaarbot.ui.theme.Black
import com.prachaarbot.ui.theme.Monstserrat

@Composable
fun PrivacyPolicyDialog(callback: (result: Boolean) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        shape = RoundedCornerShape(2.dp),
        elevation = 30.dp, backgroundColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.White)
        ) {
            Text(text = "Privacy Policy Disclosure:", style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                item {
                    Text(
                        text = "1) Your Contacts, Call logs, and the files on the External storage are safe. We NEVER COLLECT, NEVER STORE your data in any way by Unique Promotion App.",
                        fontWeight = FontWeight.Bold,
                        color = Black,
                        fontFamily = Monstserrat,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
                item {
                    Text(
                        text = "Contacts are used to avoid sharing content with contact names you wish to add to the do not disturb list.",
                        fontWeight = FontWeight.Normal,
                        color = Black,
                        fontFamily = Monstserrat,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
                item {
                    Text(
                        text = "Call log is only used if you explicitly wish to share any content.",
                        fontWeight = FontWeight.Normal,
                        color = Black,
                        fontFamily = Monstserrat,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
                item {
                    Text(
                        text = "Personal information provided by you during registration is only used to identify our users. We respect your privacy and do not use or share data with any third-party app in any manner.",
                        fontWeight = FontWeight.Normal,
                        color = Black,
                        fontFamily = Monstserrat,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
                item {
                    Text(
                        text = "Images and Video access is optional and required to share media via WhatsApp depending upon the trigger set by you.",
                        fontWeight = FontWeight.Normal,
                        color = Black,
                        fontFamily = Monstserrat,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
                item {
                    Text(
                        text = "Crash reports are used to study and improve the upcoming versions of Unique Promotion App.",
                        fontWeight = FontWeight.Normal,
                        color = Black,
                        fontFamily = Monstserrat,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
                item {
                    Text(
                        text = "2) Unique Promotion App NEVER COLLECT, NEVER STORE your Contacts, Call logs, and External storage photos and videos.",
                        fontWeight = FontWeight.Bold,
                        color = Black,
                        fontFamily = Monstserrat,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
                item {
                    Text(
                        text = "3) Accessibility permission (Accessibility service):",
                        fontWeight = FontWeight.Bold,
                        color = Black,
                        fontFamily = Monstserrat,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
                item {
                    Text(
                        text = "The Unique Promotion App is NOT affiliated with WhatsApp.",
                        fontWeight = FontWeight.Normal,
                        color = Black,
                        fontFamily = Monstserrat,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
                item {
                    Text(
                        text = "The Accessibility service is used to send WhatsApp messages automatically. And it will be ONLY used for this purpose.",
                        fontWeight = FontWeight.Normal,
                        color = Black,
                        fontFamily = Monstserrat,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
                item {
                    Text(
                        text = "The Accessibility service does NOT collect or share any information from you during your use of or access to the service.",
                        fontWeight = FontWeight.Normal,
                        color = Black,
                        fontFamily = Monstserrat,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
                item {
                    Text(
                        text = "We NEVER SHARE your personal information.",
                        fontWeight = FontWeight.Bold,
                        color = Black,
                        fontFamily = Monstserrat,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
                item {
                    Text(
                        text = "We respect your privacy and genuinely keep our promises mentioned above. Be assured, keeping your privacy is our goal.",
                        fontWeight = FontWeight.Bold,
                        color = Black,
                        fontFamily = Monstserrat,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
                item {
                    Text(
                        text = "Purpose to use, collect, and share is mentioned above in very simple English language. Still, your permission is needed once again to continue further.",
                        fontWeight = FontWeight.Bold,
                        color = Black,
                        fontFamily = Monstserrat,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
                item {
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

}
