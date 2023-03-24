# Software Engineering Project 2021/2022 (Eriantys)
## PoliMi (SWE 085923 Prof. G. Cugola)
The project received the maximum score (30L).

## Requirements implemented
<ul>
  <li>Complete Rules</li>
  <li>CLI</li>
  <li>GUI (implemented in JavaFX)</li>
  <li>Socket</li>
  <li>3 FA:
    <ul>
      <li>4 players game</li>
      <li>Persistence</li>
      <li>Resilience to disconnections</li>
    </ul>
  </li>
</ul>

## Test Coverage
<ul>
  <li>Model:
    <ul>
      <li>Class: 100%</li>
      <li>Method: 94%</li>
      <li>Line: 90%</li>
    </ul>
  </li>
  <li>Controller:
    <ul>
      <li>Class: 100%</li>
      <li>Method: 96%</li>
      <li>Line: 96%</li>
    </ul>
  </li>
</ul>

## How to run the game
<ul>
  <li>Server
    <ol>
      <li>Open the terminal and move to the folder containing EriantysServer.jar</li>
      <li>Write command <code>java -jar EriantysServer.jar</code> and press Enter</li>
      <li>Server is running!</li>
    </ol>
    <b>Note</b>: Default port is: <b>12345</b>. If you want to user another port you can execute the command
    <code>java -jar EriantysServer.jar -p &ltportNumber&gt</code>. <br>
    The range for the port is between 0 and 65535.
  </li>
  <li>Client
    <ol>
      <li>Open the terminal and move to the folder containing EriantysClient.jar</li>
      <li>
        If you want to play with the GUI version write command <code>java -jar EriantysClient.jar</code>, 
        otherwise if you want to play with the CLI version write command <code>java -jar EriantysClient.jar -cli</code>
      </li>
      <li>Play!</li>
    </ol>
    <b>Note</b>: If you are using a M1 Mac and you want to use the GUI version you have to download <code>EriantysClientM1.jar</code> and do the same         procedure as written above.
  </li>
</ul>  
    

