```
Mojava
├── README.md
├── pom.xml
└── src
    └── main
        ├── java
        │   └── net
        │       └── amar
        │           └── mojo
        │               ├── commands
        │               │   ├── slash
        │               │   │   ├── info
        │               │   │   ├── mods
        │               │   │   └── util
        │               │   └── text
        │               │       ├── fun
        │               │       ├── info
        │               │       └── moderators
        │               ├── core
        │               ├── events
        │               └── handler
        └── resources
            ├── badMods.json.example
            └── staffs.json.example
``` 

### Each directory classes

- core
Contains the main classes :
AmarLogger, a simple logger using slf4j
LoadData, a class to load stuff from ``/resources`` **MAKE SURE TO READ THE TEMPLATES IN THE RES DIR!**
MojavaMain, the main class

- handler
Contains the classes that handles some events from ``/event`` and ``/commands``

- events
Contains classes that automate some events

- commands
Contains every single command, text and slash