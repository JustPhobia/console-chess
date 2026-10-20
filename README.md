# console-chess

A playable console chess game in a single Java file: **you play White, a
minimax AI plays Black**. Unicode board, full move validation (castling, en
passant, auto-queen promotion), check / checkmate / stalemate detection, plus
`hint`, `history` and `quit` commands.

> Built within my first ~4–5 months of learning Java (a coding course). Kept as a record of progress, not as polished production code.

## Run
```bash
javac -d out src/ChessGame.java
java -cp out ChessGame
```
Enter moves in coordinate form, e.g. `e2e4`. The AI replies automatically.

## What was fixed to make it playable
It originally declared checkmate on move one. Two bugs: `inCheck` tested
attacks by the *wrong* side, and legal-move generation read the global turn
instead of the side being evaluated. Both fixed — the opening now plays
normally and mate only fires in real mating positions (verified with fool's
mate).

## Known limits (out of scope, by design)
- The AI is weak — a 3-ply material-only search, so it makes poor moves.
- Promotion is queen-only; no draw rules (50-move, repetition, insufficient
  material).
