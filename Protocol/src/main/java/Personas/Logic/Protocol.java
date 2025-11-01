package Personas.Logic;

public class Protocol {
    public static final String SERVER = "localhost";
    public static final int PORT = 1234;

    public static final int FARMACEUTA_CREATE=101;
    public static final int FARMACEUTA_READ=102;
    public static final int FARMACEUTA_UPDATE=103;
    public static final int FARMACEUTA_DELETE=104;
    public static final int FARMACEUTA_SEARCH=105;
    public static final int MEDICO_CREATE=106;
    public static final int MEDICO_READ=107;
    public static final int MEDICO_UPDATE=108;
    public static final int MEDICO_DELETE=109;
    public static final int MEDICO_SEARCH=1010;
    public static final int PACIENTE_CREATE=1011;
    public static final int PACIENTE_READ=1012;
    public static final int PACIENTE_UPDATE=1013;
    public static final int PACIENTE_DELETE=1014;
    public static final int PACIENTE_SEARCH=1015;
    public static final int RECETA_CREATE=1016;
    public static final int RECETA_READ=1017;
    public static final int RECETA_UPDATE=1018;
    public static final int RECETA_DELETE=1019;
    public static final int RECETA_SEARCH=1020;
    public static final int MEDICAMENTO_CREATE=1021;
    public static final int MEDICAMENTO_READ=1022;
    public static final int MEDICAMENTO_UPDATE=1023;
    public static final int MEDICAMENTO_DELETE=1024;
    public static final int MEDICAMENTO_SEARCH=1025;
    public static final int MEDICAMENTORECETADO_CREATE=1026;
    public static final int MEDICAMENTORECETADO_READ=1027;
    public static final int MEDICAMENTORECETADO_UPDATE=1028;
    public static final int MEDICAMENTORECETADO_DELETE=1029;
    public static final int MEDICAMENTORECETADO_SEARCH=1030;
    public static final int TRABAJADOR_READ=1031;



    //
    public static final int USER_LOGIN = 1032;
    public static final int USER_LOGOUT = 1033;
    public static final int USER_LIST = 1034;

    //
    public static final int MESSAGE_SEND = 1035;
    public static final int MESSAGE_GET_PENDING = 1036;
    public static final int MESSAGE_GET_ALL = 1037;


    public static final int ERROR_NO_ERROR=0;
    public static final int ERROR_ERROR=1;

    public static final int SYNC=10;
    public static final int ASYNC=11;
    public static final int DISCONNECT=12;
    public static final int DELIVER_MESSAGE=13;
    public static final int DELIVER_MEDICO=14;
    public static final int DELIVER_FARMACEUTA=15;
    public static final int DELIVER_PACIENTE=16;
    public static final int DELIVER_MEDICAMENTO=17;
    public static final int DELIVER_RECETA=18;
    public static final int DELIVER_MEDICAMENTORECETADO=19;

    public static final int DELIVER_USER_LOGIN = 20;
    public static final int DELIVER_USER_LOGOUT = 21;
    public static final int DELIVER_USER_MESSAGE = 22;





}
