package tank_lib;

/**
 * Classe che contienet variabili statiche per le diverse impostazione dell'app
 */
public class settings {
    public static int TILE_SIZE_PX = 30;// < dimensione di una cella in pixel
    public static int TITLE_BAR_HEIGHT = 30; // < altezza della title bar
    public static int TANK_SPEED_TILES_S = 1; // < velocita` del tank in tiles al secondo
    public static int DEFAULT_MAP_SIZE = 20; // < numero di celle di default della mappa
    public static float TANK_ROTATION_SPEED_RPM = 0.5f; // < velocita` di rotazione del tank su se stesso in giri al
                                                        // minuto
    public static int REFRESH_RATE = 10; // < millisecondi che intercorrono tra un aggiornamento e l'altro della grafica
    public static int NUMBER_OF_CLIENTS = 1; // < numero di client che si connettono al server
}
