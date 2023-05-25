package com.example.mercadolaboral2.data.local.constants;

import com.example.mercadolaboral2.utils.CsproJson;

import gov.census.cspro.csentry.fileaccess.FileAccessHelper;

public class AppConstants {

//    public static final String DIRECTORIO_CSPRO = "/storage/emulated/0/Android/data/gov.census.cspro.csentry/";

    public static final String APP_CSPRO = "gov.census.cspro.csentry";
    public static final String NOMBRE_PEN = "cen2020.pen";
    public static final String NOMBRE_PFF = "CEN2020.pff";
     //    public static final String BASE_URL = "https://8770-200-46-58-8.ngrok.io/api/";
    //Prueba
    //public static final String BASE_URL = "https://www.censospanama.pa/censos-api/api/";
//        public static final String BASE_URL = "https://www.censospanama.pa/dev-api/api/";

     //public static final String BASE_URL = "http://inec-dev-03:420";
     public static final String BASE_URL = "http://172.16.9.27:420/api/";

    public static final String BASE_URL_FTP = "https://www.censospanama.pa/ftp-api/FTP/";
    public static final String BASE_URL_MAPS = "https://www.censospanama.pa/censos-mapas/Mapas/";
//    public static final String BASE_URL_MAPS = "https://www.censospanama.pa/dev-mapas/Mapas/";
//        public static final String BASE_URL = "http://172.16.9.27:82/api/";
//    public static final String BASE_URL_MAPS = "http://172.16.9.27:85/Mapas/";

    public static final String PREF_ID = "PREF_ID";
    public static final String PREF_USERNAME = "PREF_USERNAME";
    public static final String PREF_CODIGO = "PREF_CODIGO";
    public static final String PREF_PASSWORD = "PREF_PASSWORD";
    public static final String PREF_ROL = "PREF_ROL";
    public static final String PREF_ACCESS_TOKEN = "PREF_ACCESS_TOKEN";

    public static final String PREF_REFRESH_TOKEN = "PREF_REFRESH_TOKEN";
    public static final String PREF_EXPIREAT = "PREF_EXPIREAT";

    public static final String PREF_LOG_USER = "PREF_LOG_USER";
    public static final String PREF_FECHA = "PREF_FECHA";
    // --Commented out by Inspection (06/10/2021 1:51 p. m.):public static final String PREF_LOGGED_USER = "PREF_LOGGED_USER";

//    public static final String PREF_DICCIONARIO = "PREF_DICCIONARIO";

    public static final String TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String TIMESTAMP_PATTERN_ESTADOS = "yyyyMMddHHmm";

    public static final String SEG_ESTADO_ENPROCESO = "EN PROCESO";
    public static final String SEG_ESTADO_HABILITADO = "HABILITADO";
    public static final String SEG_ESTADO_CERRADO_COMPLETO = "CERRADO COMPLETO";
    public static final String SEG_ESTADO_CERRADO_INCOMPLETO = "CERRADO INCOMPLETO";

    public static final String PREF_PERMISSION = "PREF_PERMISSION";
    public static final String PREF_BACKUP_WORKER = "PREF_BACKUP_WORKER";
    public static final String CODIGO_ELIMINAR_OTRASESTRUCTURAS = "dtoexxii@";//TODO QUE SEA DINAMICO
    public static final String CODIGO_ELIMINAR_CUESTIONARIO = "ieecxx!";//TODO QUE SEA DINAMICO
    public static final String CODIGO_ECENSO = "dsemm@";//TODO QUE SEA DINAMICO
    public static final String CODIGO_RECUPERACIONCUESTIONARIOS = "";//TODO QUE SEA DINAMICO
    public static final String CODIGO_ACTUALIZARSEGMENTOS = "eircmmxx!!";//TODO QUE SEA DINAMICO
    public static FileAccessHelper fileAccessHelper;
    public static CsproJson csproJson;

    public static final String ESTADOS_STATUS = "ESTADOS_STATUS";
}
