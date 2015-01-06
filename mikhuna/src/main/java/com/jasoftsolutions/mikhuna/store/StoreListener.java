package com.jasoftsolutions.mikhuna.store;

import android.support.annotation.Nullable;

/**
 * Created by pc07 on 26/08/2014.
 *
 */
public interface StoreListener {

    /**
     * Se invoca cuando al momento del request ya se tiene un objeto completo (de una petición anterior)
     * Se debe notificar únicamente al que hizo el request
     * @param sender
     * @param data      El objeto que está almacenado en la base de datos actualmente (sin actualizar)
     */
    void onReady(Object sender, Object data);

    /**
     * Se invoca cuando se ha obtenido correctamente el objeto desde el servidor remoto
     * Se le notifica a todos los oyentes
     * @param sender
     * @param data
     */
    void onUpdate(Object sender, Object data);

    /**
     * Se ejecuta al momento en que la petición ha tardado más del tiempo máximo que se le ha especificado
     * Se debe notificar únicamente al que hizo el request
     * @param sender
     * @param data      El objeto que está almacenado en la base de datos actualmente (sin actualizar)
     */
    void onTimeOut(Object sender, @Nullable Object data);

    /**
     * Notifica que la petición hecha al servidor remoto ha fallado
     * @param sender
     * @param data      El objeto que está almacenado en la base de datos actualmente (sin actualizar)
     */
    void onFailedConnection(Object sender, @Nullable Object data);

    /**
     * Notifica que ha sucedido un éxito (sucede luego de un onReady o un onUpdate)
     * @param sender
     * @param data
     */
    void onSuccess(Object sender, Object data);

    /**
     * Notifica que ha sucedido un fallo (sucede luego de un onTimeOut o un onFailedConnection)
     * @param sender
     * @param data
     */
    void onFail(Object sender, Object data);
}
