package Player;

/**
 * There are following modes
 *
 * playerVsRemote       We have got an own and enemy playground
 *
 * KiVsRemote           Our Ki plays, we have got an own and enemy playground
 *
 * playerVsKi           We play in Singleplayer against our own KI, the player got an own and enemy playground. The Ki also got an enemy and own playground
 *
 * KivsKi               Our KI plays against the enemy KI in Singleplayer, both KI´s got an own and enemy playground, the own KI`s playgrounds are shown at the gui
 */
public enum GameMode {
    playerVsRemote,
    kiVsRemote,
    playerVsKi,
    kiVsKi
}
