package Network;

/**
 * All commands allowed by the communication protocol
 * Inclusive the extended mode commands, these commands are also checked
 * by the cmdChecker in Communication but not implemented, as no one else implemented the enhanced version
 */
public enum CMD {
    size,
    ships,
    shot,
    answer,
    save,
    load,
    next,
    done,
    ready,
    timeout,
    name,
    repeat,
    firstShot
}
