package mx.com.qrplus.qrplusaccesos.Helpers;

public class Global {

    //Variables Login Usuario determinado de la aplicación
    public static int usuario_Id;
    public static int perfil_Id;
    public static int tipo_id;
    public static int domicilio_Id;
    public static int Usu_Id;

    //    Variables de acceso al WCF
    public static String usuarioWCF = "*Acciona*Jar";
    public static String passwordWCF = "*Acciona*Jar";

    public static String correoUsuario;

    public static String URL_DOMAIN = "http://18.217.131.67/WCFQRPLUS/Service1.svc";
    public static String NAMESPACE = "http://tempuri.org/";
    public static String SOAP_ACTION = "http://tempuri.org/IServicio/";

    public static String METHOD_NAME_LOGIN = "mAIDE_Login";
    public static String METHOD_NAME_RESET_PASSWORD ="mAIDE_RecuperaContraseña";
    public static String METHOD_NAME_SELECT_EVENT = "mAIDE_ConsultaEvento";
    public static String METHOD_NAME_INSERT_EVENT = "mAIDE_InsertaEvento";
    public static String METHOD_NAME_EDIT_EVENT = "mAIDE_EditaEvento";
    public static String METHOD_NAME_REGISTER_INVITED = "mAIDE_RegistraInvitados";
    public static String METHOD_NAME_INSERT_PEOPLE_EXP = "mAIDE_InsertarPersonaExp";
    public static String METHOD_NAME_UPDATE_INVITED = "mAIDE_ActualizaInvitado";
    public static String METHOD_NAME_DELETE_INVITED = "mAIDE_EliminaInvitado";
    public static String METHOD_NAME_LIST_INVITED = "mAIDE_Listainvitados";
    public static String METHOD_NAME_LIST_INVITED_EVENT = "mAIDE_ListaInvitadosEnEvento";
    public static String METHOD_NAME_DOCUMENTS_LEGAL = "mAIDE_DocumentosLegales";
    public static String METHOD_NAME_CHANSE_PASSWORD = "mAIDE_CambiaContraseña";

    public static int NoIdEvento;


}

