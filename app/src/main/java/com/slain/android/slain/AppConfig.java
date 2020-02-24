package com.slain.android.slain;

public class AppConfig {
//    public static final String ROOT_URL = "http://192.168.1.8/SLA.in/";
    public static final String ROOT_URL = "http://192.168.43.32/SLA.in/";
//    public static final String ROOT_URL = "http://192.168.1.10/SLA.in/";
//    public static final String ROOT_URL = "http://farport.000webhostapp.com/JEMPOL.in/";
//    public static final String ROOT_URL = "http://www.umkmbanten.info/JEMPOL.in/";

    public static final String URL_REGISTER = ROOT_URL + "api1.php?apicall=signup";
    public static final String URL_LOGIN = ROOT_URL + "api1.php?apicall=login";
    public static final String IMAGE_URL = ROOT_URL + "api.php?apicall=";
    public static final String UPLOAD_URL = IMAGE_URL + "uploadpic";
    public static final String GET_PICS_URL = IMAGE_URL + "getpics";
    public static final String URL_LIST_LAPORAN = ROOT_URL + "getLaporanHead.php";
    public static final String URL_PENDING_LAPORAN = ROOT_URL + "getPendingLaporan.php";
    public static final String URL_PROCESS_LAPORAN = ROOT_URL + "getProcessLaporan.php";
    public static final String URL_COMPLETED_LAPORAN = ROOT_URL + "getCompletedLaporan.php";
    public static final String URL_DELETE_LAPORAN = ROOT_URL + "deleteLaporan.php";
    public static final String URL_PROSES_LAPORAN = ROOT_URL + "prosesLaporan.php";
    public static final String URL_SELESAI_LAPORAN = ROOT_URL + "selesaiLaporan.php";
    public static final String URL_SET_RAPAT = ROOT_URL + "setRapat.php";
    public static final String URL_GET_RAPAT_LIST = ROOT_URL + "getRapat.php";
    public static final String URL_GET_RAPATKU = ROOT_URL + "getRapatku.php";
    public static final String URL_DELETE_RAPAT_USER = ROOT_URL + "deleteRapatUser.php";
    public static final String URL_GET_RAPAT_TODAY = ROOT_URL + "getRapatToday.php";
    public static final String URL_GET_RAPAT_PENDING = ROOT_URL + "getRapatPending.php";
    public static final String URL_UPDATE_RAPAT = ROOT_URL + "updateRapat.php";
    public static final String URL_UPDATE_RAPAT_USER = ROOT_URL + "updateRapatUser.php";
    public static final String URL_ACC_RAPAT = ROOT_URL + "accRapat.php";
    public static final String URL_ACC_USER = ROOT_URL + "accUser.php";
    public static final String URL_GET_USER = ROOT_URL + "getUser.php";
    public static final String URL_GET_USER_NON = ROOT_URL + "getUserNonAktif.php";
    public static final String URL_DELETE_USER = ROOT_URL + "deleteUser.php";
    public static final String URL_GET_LAPORAN_DONE = ROOT_URL + "getLaporanDone.php";
    public static final String URL_SET_MOBIL = ROOT_URL + "setMobil.php";
    public static final String URL_GET_MOBIL = ROOT_URL + "getMobil.php";
    public static final String URL_GET_MOBIL_PENDING = ROOT_URL + "getMobilPending.php";
    public static final String URL_GET_MOBIL_ADMIN = ROOT_URL + "getMobilAdmin.php";
    public static final String URL_DELETE_MOBIL = ROOT_URL + "deleteMobil.php";
    public static final String URL_UPDATE_MOBIL_USER = ROOT_URL + "updateMobilUser.php";
    public static final String URL_PROSES_MOBIL = ROOT_URL + "prosesMobil.php";
    public static final String URL_UPDATE_MOBIL_ADMIN = ROOT_URL + "updateMobilAdmin.php";
    public static final String URL_SET_BARANG = ROOT_URL + "setBarang.php";
    public static final String URL_GET_BARANG = ROOT_URL + "getBarang.php";
    public static final String URL_GET_BARANG_ADMIN = ROOT_URL + "getBarangAdmin.php";
    public static final String URL_GET_BARANG_PENDING = ROOT_URL + "getBarangPending.php";
    public static final String URL_DELETE_BARANG = ROOT_URL + "deleteBarang.php";
    public static final String URL_UPDATE_BARANG_USER = ROOT_URL + "updateBarangUser.php";
    public static final String URL_UPDATE_BARANG_ADMIN = ROOT_URL + "updateBarangAdmin.php";
    public static final String URL_ACC_BARANG = ROOT_URL + "accBarang.php";
    public static final String URL_EDIT_AKUN = ROOT_URL + "editAkun.php";




    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_firebase";
}
