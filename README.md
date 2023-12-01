# Battle tanks destroy max 600 turbo

![Lobby](https://github.com/bas-ene/Battle-tanks-destroy-max-600-turbo/assets/68295216/5d237a0e-59fd-4240-a964-151ab66afbc3)\
Lobby

![MainGame](https://github.com/bas-ene/Battle-tanks-destroy-max-600-turbo/assets/68295216/bcd80f0c-a0a3-4cfa-a543-37e2b46ef469)\
Mappa di gioco con due giocatori

## Panoramica del progetto

Questo e' un 2D top-down view shooter tra carri armati, dove 2 o piu' giocatori possono affrontarsi in una mappa generata proceduralmente, diversa ogni volta. La mappa e' costituita da diversi tipi di terreno, tra i quali gli edifici, che possono essere distrutti dai proiettili.

### Scaricare il gioco

```
git clone https://github.com/bas-ene/Battle-tanks-destroy-max-600-turbo/
```

### Avviare il gioco

- ##### Da VS Code

  - Avviare un'istanza di Server.java
  - Avviare `settings.java` istanze di Client.java, inserendo nella lobby l'indirizzo e la porta del Server.

- ##### Tramite CLI
  -Compilare il client con `javac client/Client.java`
  -Compilare il server con `javac server/Server.java`
  -Avviare il server con `java server.Server`
  -Avviare `settings.java` client ripetendo altrettante volte il comando `java client.Client`

### Gameplay

Una volta connessi al server, bisogna aspettare che gli altri giocatori si connettano. Dopodiche', i giocatori potranno iniziare a muoversi a e spararsi. Ovviamente non possono uscire dalla mappa ne muoversi sugli edifici, che tuttavia potranno essere distruttti. Una volta che un giocatore finisce i punti vita, perde e si disconnete. L'ultimo giocatore in vita vince.

### Come giocare

Il carro armato si muove con `WASD`, mentre spara (in fronte a se) con `Z`.

### Impostazioni

Molti parametri possono essere modificati dal file settings.java nel package `tank_lib`, come velocita` di rotazione e di movimento, numero di giocatori e dimensioni della mappa.

---

### Known issues e feature future

- Il movimemento dei proiettili nei client non corrisponde a quello del server, cio' e' dovuto dalla mancaza della gestione della loro velictita' con il meccanismo del delta time.
- Power up
- A volte due client "possiedono" lo stesso tank
- Fine del gioco non implementata del tutto correttamente server-side, per qualche motivo non manda il pacchetto di fine partita nel modo corretto
- Vero Wave Function Collapse

---

#### Contributors

> [Enea Basilico](https://github.com/bas-ene/)
> [Evan Gervasio](https://github.com/evangerva/)
