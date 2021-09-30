## The Secret Service App
___
This application is a **joky app** based on the idea that somebody decided
to become a secret agent to carry out secret missions for the benefit
of **fictional Android Community**, and he can receive his first mission with the help of this app.
___
To get the details of his first mission the user must install and launch this app.

When the user launches an app, the **StartActivity** sends an intent to launch the **LoginActivity** which checks whether the  **loginTable** contains any login and password and launches the screen in sign up or login configuration.

In the *sign up configuration* the user
 passes his name and password and this data is written to the SQLite **logintable**, which contains only one row, e.g.:
 
 No | Name | Password
--- |--- | ---
1|Billy|fg34Kl38

In the *login configuration* user can enter his name and password. 

After pressing the *LOGIN* button, the **LoginActivity** checks the validity of entered name and password and whether the **Data** table of the **DataContentProvider** is empty. 

- In case the **Data** table is empty (user launches app for the first time),  the table is filled in with the data of five different missions to choose from:

No|Name|Lat|Lng|Address|URL
---|---|---|---|---|---
1|The White House|   |   |  1600 Pennsylvania Avenue NW, Washington |
2|The Moscow Kremlin|||Moscow Kremlin, Moscow, Russian Federation|
3|The 10 Downing Street|||10, Downing Street, City of Westminster, London|
4|The Government House, Wellington|||Wellington Mail Centre, Lower Hutt 5045|
5|The State House of Namibia|||Windhoek, Namibia|

Then the **LoginActivity** produces an intent to launch the **MainActivity**,  which in turn launches the **LoadService** to  take randomly one of the rows of **Data** table of the **DataContentProvider** and write this data to the next row of the **Data** table in order to use it as the data of the mission, e.g.:

No|Name|Lat|Lng|Address|URL
---|---|---|---|---|---
||||||
6|The 10 Downing Street|||10, Downing Street, City of Westminster, London

After that the **LoadService** produces a broadcast intent, which is received by the broadcast receiver **ViewReceiver**, which in turn launches the **ViewActivity**.

- In case the **Data** table is not empty (the user has already got his mission), the **LoginActivity** launches the **ViewActivity**.

The **ViewActivity** allows user to read the instructions for the first mission, and shows the photo of the mission or launches the **MapActivity**  to show the map of the place of the mission depending on button pressed.</br>
The URL address of the photo and the  geographical coordinates of the place of the mission are taken from the fifth line of the table of the **DataContentProvider**.
___
The **Secret Service App** will be connecting to the following **web services**:</br>

- https://en.wikipedia.org/wiki/
- https://www.google.com/maps/</br>
___














++++