Ap-Project Summer 2026
Parsa Moradi
40413022
https://github.com/Pqrsaw/Chicken-Invaders-AP
Implementing Chicken Invaders

How to play :

+-----------------------------------------------------------+
|                      CONTROLS                             |
+-----------------------------------------------------------+
|  Arrow Keys / WASD     - Move the plane                   |
|  Space                 - Shoot                            |
|  P                     - Pause / Resume                   |
|  ESC                   - Return to menu                   |
+-----------------------------------------------------------+
|                      POWER-UPS                            |
+-----------------------------------------------------------+
|  Add Fire (+)          - Increases number of shots (Perm.)|
|  Rapid Fire (F)        - Increases fire rate (8s)         |
|  Extra Life (♥)        - Restores one life (Perm.)        |
|  Shield (S)            - Invincibility (10s)              |
|  Freeze Bomb (❄)       - Freezes all enemies and eggs (3s)|
+-----------------------------------------------------------+
|                      GAME INFO                            |
+-----------------------------------------------------------+
|  Defeat all enemies in each level to advance              |
|  Defeat bosses at levels 4 and 8                          |
|  Protect your plane from falling eggs                     |
|  Collect power-ups to gain advantages                     |
|  If any chicken reaches the bottom, game is over          |
+-----------------------------------------------------------+
|                      SCORING                              |
+-----------------------------------------------------------+
|  Normal Chicken        -   10 points                      |
|  Fast Chicken          -   15 points                      |
|  Zigzag Chicken        -   20 points                      |
|  Shooter Chicken       -   25 points                      |
|  Level Complete        -  200 bonus points                |
|  Boss Level 4          -  500 bonus points                |
|  Boss Level 8          - 1000 bonus points                |
+-----------------------------------------------------------+
|                      LEVELS                               |
+-----------------------------------------------------------+
|  Level 1   - Normal Chickens                              |
|  Level 2   - Normal + Fast Chickens                       |
|  Level 3   - Normal + Zigzag Chickens                     |
|  Level 4   - Boss (4 direction shooting)                  |
|  Level 5   - Shooter + Fast Chickens                      |
|  Level 6   - Zigzag + Shooter Chickens                    |
|  Level 7   - All types (No boss)                          |
|  Level 8   - Final Boss (8 direction shooting)            |
+-----------------------------------------------------------+
|                      Good luck!                           |
+-----------------------------------------------------------+

Database : 

game.db => users : id (pk) / username / password / high_score / last_level /
(sound settings)   bgm_enabled / shot_enabled / crash_enabled / gameover_enabled /
                   selected_plane

    game_history : id (pk) / username / score / level / play_date /
(sound settings)   bgm_enabled / shot_enabled / crash_enabled / gameover_enabled /
                  
