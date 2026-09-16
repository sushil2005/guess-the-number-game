package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ColorCorrect
import com.example.ui.theme.ColorHigh
import com.example.ui.theme.ColorLow
import com.example.ui.theme.ColorWarning
import com.example.ui.theme.IndigoDark
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.MyApplicationTheme

enum class MessageType {
  None,
  TooHigh,
  TooLow,
  Correct,
  Warning
}

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        Scaffold(
          modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .imePadding()
        ) { innerPadding ->
          GuessTheNumberScreen(modifier = Modifier.padding(innerPadding))
        }
      }
    }
  }
}

@Composable
fun GuessTheNumberScreen(modifier: Modifier = Modifier) {
  var targetNumber by remember { mutableIntStateOf((1..100).random()) }
  var attempts by remember { mutableIntStateOf(0) }
  var guessInput by remember { mutableStateOf("") }
  var resultMessage by remember { mutableStateOf("") }
  var messageType by remember { mutableStateOf(MessageType.None) }
  var isWon by remember { mutableStateOf(false) }

  fun submitGuess() {
    if (isWon) return

    val raw = guessInput.trim()
    if (raw.isEmpty()) {
      resultMessage = "Please enter a number."
      messageType = MessageType.Warning
      return
    }

    val guess = raw.toIntOrNull()
    if (guess == null || guess < 1 || guess > 100) {
      resultMessage = "Enter a number between 1 and 100."
      messageType = MessageType.Warning
      return
    }

    attempts++
    when {
      guess > targetNumber -> {
        resultMessage = "Too High"
        messageType = MessageType.TooHigh
      }
      guess < targetNumber -> {
        resultMessage = "Too Low"
        messageType = MessageType.TooLow
      }
      else -> {
        resultMessage = "🎉 Correct!"
        messageType = MessageType.Correct
        isWon = true
      }
    }
  }

  fun newGame() {
    targetNumber = (1..100).random()
    attempts = 0
    guessInput = ""
    resultMessage = ""
    messageType = MessageType.None
    isWon = false
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(16.dp),
    contentAlignment = Alignment.Center
  ) {
    Card(
      modifier = Modifier
        .widthIn(max = 440.dp)
        .fillMaxWidth()
        .shadow(
          elevation = 12.dp,
          shape = RoundedCornerShape(20.dp),
          ambientColor = Color.Black.copy(alpha = 0.08f),
          spotColor = Color.Black.copy(alpha = 0.12f)
        )
        .testTag("game_card"),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface
      )
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState())
          .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Title
        Text(
          text = "Guess The Number",
          style = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.5).sp
          ),
          color = MaterialTheme.colorScheme.onSurface,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Subtitle
        Text(
          text = "Guess a number between 1 and 100",
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Input Field
        OutlinedTextField(
          value = guessInput,
          onValueChange = { input ->
            if (input.all { it.isDigit() } && input.length <= 3) {
              guessInput = input
            }
          },
          placeholder = {
            Text(
              "Enter a number",
              modifier = Modifier.fillMaxWidth(),
              textAlign = TextAlign.Center
            )
          },
          singleLine = true,
          enabled = !isWon,
          textStyle = MaterialTheme.typography.titleLarge.copy(
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold
          ),
          keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
          ),
          keyboardActions = KeyboardActions(
            onDone = { submitGuess() }
          ),
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = IndigoPrimary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline
          ),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("guess_input")
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Guess Button
        Button(
          onClick = { submitGuess() },
          enabled = !isWon,
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = IndigoPrimary,
            contentColor = Color.White,
            disabledContainerColor = Color(0xFFCBD5E1),
            disabledContentColor = Color(0xFF64748B)
          ),
          modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .testTag("guess_button")
        ) {
          Text(
            text = "Guess",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
          )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Result Message
        this@Column.AnimatedVisibility(
          visible = resultMessage.isNotEmpty(),
          enter = fadeIn() + scaleIn(initialScale = 0.9f),
          exit = fadeOut(),
          modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 36.dp)
        ) {
          val textColor = when (messageType) {
            MessageType.TooHigh -> ColorHigh
            MessageType.TooLow -> ColorLow
            MessageType.Correct -> ColorCorrect
            MessageType.Warning -> ColorWarning
            MessageType.None -> MaterialTheme.colorScheme.onSurface
          }
          Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = resultMessage,
              style = if (messageType == MessageType.Correct) {
                MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
              } else {
                MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
              },
              color = textColor,
              textAlign = TextAlign.Center,
              modifier = Modifier.testTag("result_message")
            )
          }
        }

        if (resultMessage.isEmpty()) {
          Spacer(modifier = Modifier.height(36.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Attempts Counter
        Row(
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.testTag("attempts_counter")
        ) {
          Text(
            text = "Attempts: ",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Text(
            text = attempts.toString(),
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // New Game Button
        OutlinedButton(
          onClick = { newGame() },
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .testTag("new_game_button")
        ) {
          Text(
            text = "New Game",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.SemiBold,
              color = MaterialTheme.colorScheme.onSurface
            )
          )
        }
      }
    }
  }
}

// Backward compatibility for existing template screenshot test
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  GuessTheNumberScreen(modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun GuessTheNumberPreview() {
  MyApplicationTheme {
    GuessTheNumberScreen()
  }
}

