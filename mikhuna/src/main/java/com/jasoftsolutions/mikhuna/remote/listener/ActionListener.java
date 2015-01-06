package com.jasoftsolutions.mikhuna.remote.listener;

/**
 * Created by pc07 on 14/05/2014.
 */
public interface ActionListener {

    /**
     * Notifica el suceso de un evento determinado
     * @param sender El objeto que notificó el evento
     * @param action
     * @return True si se quiere seguir escuchando el evento, o False para que el oyente
     * sea dado de baja automáticamente
     */
    boolean actionPerformed(Object sender, int action);

}
