// Guess The Number - Game Logic

// Game State Variables
let targetNumber = 0;
let attempts = 0;
let isGameOver = false;

// DOM Element References
const guessForm = document.getElementById("guess-form");
const guessInput = document.getElementById("guess-input");
const guessBtn = document.getElementById("guess-btn");
const resultMessage = document.getElementById("result-message");
const attemptsCount = document.getElementById("attempts-count");
const newGameBtn = document.getElementById("new-game-btn");

/**
 * Initializes or resets the game state
 */
function initGame() {
  // Generate random integer between 1 and 100 (inclusive)
  targetNumber = Math.floor(Math.random() * 100) + 1;
  attempts = 0;
  isGameOver = false;

  // Reset UI elements
  attemptsCount.textContent = "0";
  resultMessage.textContent = "";
  resultMessage.className = "result-message";
  guessInput.value = "";
  guessInput.disabled = false;
  guessBtn.disabled = false;
  guessInput.focus();
}

/**
 * Displays a result or validation message
 * @param {string} text - Message text
 * @param {string} typeClass - CSS class for color styling
 */
function displayMessage(text, typeClass) {
  resultMessage.textContent = text;
  resultMessage.className = `result-message visible ${typeClass}`;
}

/**
 * Validates user input and processes the guess
 */
function handleGuess(event) {
  if (event) {
    event.preventDefault();
  }

  // If the game is already won, ignore further guesses
  if (isGameOver) {
    return;
  }

  const rawValue = guessInput.value.trim();

  // Validation 1: Empty input
  if (rawValue === "") {
    displayMessage("Please enter a number.", "warning");
    guessInput.focus();
    return;
  }

  const guess = Number(rawValue);

  // Validation 2: Out of range (below 1 or above 100) or not a valid number
  if (isNaN(guess) || guess < 1 || guess > 100) {
    displayMessage("Enter a number between 1 and 100.", "warning");
    guessInput.focus();
    return;
  }

  // Valid guess: count this attempt
  attempts++;
  attemptsCount.textContent = attempts.toString();

  // Compare guess to the secret target number
  if (guess > targetNumber) {
    displayMessage("Too High", "too-high");
    guessInput.focus();
    guessInput.select();
  } else if (guess < targetNumber) {
    displayMessage("Too Low", "too-low");
    guessInput.focus();
    guessInput.select();
  } else {
    // Winner!
    displayMessage("🎉 Correct!", "correct");
    isGameOver = true;
    guessBtn.disabled = true;
    guessInput.disabled = true;
  }
}

// Event Listeners
guessForm.addEventListener("submit", handleGuess);
newGameBtn.addEventListener("click", initGame);

// Ensure pressing Enter inside the input field submits the guess
guessInput.addEventListener("keydown", function (e) {
  if (e.key === "Enter") {
    e.preventDefault();
    handleGuess();
  }
});

// Start game automatically on page load
window.addEventListener("DOMContentLoaded", initGame);
