# PacMan Remake - Java Swing Framework
This is a custom implementation of the classic PacMan game, developed as part of a uni project, with emphasis of using Java Swing framework!

![Desktop Screenshot 2024 09 24 - 21 08 13 71](https://github.com/user-attachments/assets/701eccd0-208d-4ecc-bcf6-87f86d223d0d) 

## Game Rules & Controls 

- **The Game of PacMan**:
    - use **arrow keys** on keyboard to move PacMan
    - your objective is to eat all points/pallets
    - you start with 3 lives
    - avoid 4 scary ghosts and get upgrades to eat them and get big points
    - hitting a ghost in the normal state results in losing a life; losing all lives results in game over
    - gather as many points as possible and beat highscores!

The objective is straight forward, however winning is not so easy. 

## Features

![Desktop Screenshot 2024 09 24 - 21 08 19 83](https://github.com/user-attachments/assets/e9f45498-df88-4d3c-8ca8-4471c7a54aa4)

- **Main Menu:** 
    - play on selected map
    - view highscores
    - exit application

- **Boards**: 
    - Standard
    - Small
    - Tower
    - Slim
    - Large

- **Upgrades**: 
    - speed boost 
    - freeze
    - flash 
    - slowness 
    - _(+1)_ life 

- **Game/UI**
    - Score Counter
    - Life Counter
    - all synchronization related to time passage done with `Thread Class` 
    - scalable window

- **Highscore Table**:
    - add new scores with username
    - view previous scores
    - scrollable 
    - implemented using `JList`

- **PacMan**:
    - animation using `BufferImage` sequence & rotation

- **Ghosts**:
    - normal state _(purple)_
    - weak state _(blue)_
    - tries to move in its current direction 65% of the time
    - If it can't move forward, or if random chance decides otherwise, it picks a random valid direction.
    - It can reverse direction if the only available move is reverse.
    - If it goes off one side of the board, it reappears on the opposite side.

## Additional Remarks
It is important to note that this is the orginial implementation that was towards a given deadline, so given the time limitation, the implementation is no near perfect, sometimes issues/bugs can occur. I might do a future commit to fix these issues and possibly polish the game too. If so, installation guide will be added too. Still, it was a great learning experience!
