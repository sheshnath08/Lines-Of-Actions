# AI-Project : Lines of Actions 5X5 version
Lines of Actions is a two player board game and developed as a part of course project CS-GY 6613.
<h3>Game Rules</h3>
• The first player to bring all of his/her checkers together into an 8-connected contiguous body wins. If a player has only one checker left, he/she also wins.<br>
• Black player moves first and the 2 players alternate moves.<br>
• Checkers can move horizontally, vertically, or diagonally.<br>
• A checker moves exactly as many spaces as there are checkers (both friendly and enemy) on the line in which it is moving.<br>
• A checker may jump over friendly checkers, but not over enemy checkers. <br>
• A checker may capture an enemy checker by landing on the current position of the enemy checker. <br>
• If after a capturing move, both players have their pieces in a contiguous body, then the player who makes the move wins.<br>

<br>
<h3>How to Run:</h3>
You can compile classes in package View and AI under LinesofAction/src/ dir through terminal/command prompt using following command:<br>
    o javac View/*.java<br>
    o javac AI/*.java<br>
After compilation, run Controller from package AI using following command:<br>
    o java AI/Controller<br>
