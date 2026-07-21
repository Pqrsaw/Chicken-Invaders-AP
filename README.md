# Chicken Invaders - AP Project

**Summer 2026**  
**Parsa Moradi**  
**Student ID: 40413022**  
**GitHub:** https://github.com/Pqrsaw/Chicken-Invaders-AP

---

## About
Implementing Chicken Invaders game in java.

---

## How to Play

### Controls
| Key | Action |
|-----|--------|
| Arrow Keys / WASD | Move the plane |
| Space | Shoot |
| P | Pause / Resume |
| ESC | Return to menu |

### Power-Ups
| Power-Up | Symbol | Effect | Duration |
|----------|--------|--------|----------|
| Add Fire | (+) | Increases number of shots | Permanent |
| Rapid Fire | (F) | Increases fire rate | 8 seconds |
| Extra Life | (♥) | Restores one life | Permanent |
| Shield | (S) | Invincibility | 10 seconds |
| Freeze Bomb | (❄) | Freezes all enemies and eggs | 3 seconds |

### Scoring
| Enemy Type | Points |
|------------|--------|
| Normal Chicken | 10 points |
| Fast Chicken | 15 points |
| Zigzag Chicken | 20 points |
| Shooter Chicken | 25 points |
| Level Complete | 200 bonus points |
| Boss Level 4 | 500 bonus points |
| Boss Level 8 | 1000 bonus points |

### Game Info
- Defeat all enemies in each level to advance
- Defeat bosses at levels 4 and 8
- Protect your plane from falling eggs
- Collect power-ups to gain advantages
- If any chicken reaches the bottom, game is over

### Levels
| Level | Enemy Types |
|-------|-------------|
| Level 1 | Normal Chickens |
| Level 2 | Normal + Fast Chickens |
| Level 3 | Normal + Zigzag Chickens |
| Level 4 | Boss (4 direction shooting) |
| Level 5 | Shooter + Fast Chickens |
| Level 6 | Zigzag + Shooter Chickens |
| Level 7 | All types (No boss) |
| Level 8 | Final Boss (8 direction shooting) |

---

## Database

### Users Table (`users`)
| Column | Type | Description |
|--------|------|-------------|
| id | INTEGER (PK) | Primary key |
| username | TEXT | User's username |
| password | TEXT | User's password |
| high_score | INTEGER | Highest score achieved |
| last_level | INTEGER | Last level reached |
| bgm_enabled | BOOLEAN | Background music toggle |
| shot_enabled | BOOLEAN | Shot sound toggle |
| crash_enabled | BOOLEAN | Crash sound toggle |
| gameover_enabled | BOOLEAN | Game over sound toggle |
| selected_plane | TEXT | Currently selected plane |

### Game History Table (`game_history`)
| Column | Type | Description |
|--------|------|-------------|
| id | INTEGER (PK) | Primary key |
| username | TEXT | Player's username |
| score | INTEGER | Score achieved |
| level | INTEGER | Level reached |
| play_date | DATETIME | Date and time played |
| bgm_enabled | BOOLEAN | Background music setting |
| shot_enabled | BOOLEAN | Shot sound setting |
| crash_enabled | BOOLEAN | Crash sound setting |
| gameover_enabled | BOOLEAN | Game over sound setting |

---

## Good luck!
                  
                  
