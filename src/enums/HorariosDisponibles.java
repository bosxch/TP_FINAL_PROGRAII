package enums;

import java.time.LocalTime;

/**
 * Enumerador que define los horarios de inicio de los SLOTS de 15 minutos
 * para facilitar la creacion de agendas de los profesionales.
 */
public enum HorariosDisponibles {

    // MAÑANA (07:00 a 12:45)
    H_0700(LocalTime.of(7, 0)),
    H_0715(LocalTime.of(7, 15)),
    H_0730(LocalTime.of(7, 30)),
    H_0745(LocalTime.of(7, 45)),

    H_0800(LocalTime.of(8, 0)),
    H_0815(LocalTime.of(8, 15)),
    H_0830(LocalTime.of(8, 30)),
    H_0845(LocalTime.of(8, 45)),

    H_0900(LocalTime.of(9, 0)),
    H_0915(LocalTime.of(9, 15)),
    H_0930(LocalTime.of(9, 30)),
    H_0945(LocalTime.of(9, 45)),

    H_1000(LocalTime.of(10, 0)),
    H_1015(LocalTime.of(10, 15)),
    H_1030(LocalTime.of(10, 30)),
    H_1045(LocalTime.of(10, 45)),

    H_1100(LocalTime.of(11, 0)),
    H_1115(LocalTime.of(11, 15)),
    H_1130(LocalTime.of(11, 30)),
    H_1145(LocalTime.of(11, 45)),

    H_1200(LocalTime.of(12, 0)),
    H_1215(LocalTime.of(12, 15)),
    H_1230(LocalTime.of(12, 30)),
    H_1245(LocalTime.of(12, 45)),

    // TARDE (13:00 a 16:45)
    H_1300(LocalTime.of(13, 0)),
    H_1315(LocalTime.of(13, 15)),
    H_1330(LocalTime.of(13, 30)),
    H_1345(LocalTime.of(13, 45)),

    H_1400(LocalTime.of(14, 0)),
    H_1415(LocalTime.of(14, 15)),
    H_1430(LocalTime.of(14, 30)),
    H_1445(LocalTime.of(14, 45)),

    H_1500(LocalTime.of(15, 0)),
    H_1515(LocalTime.of(15, 15)),
    H_1530(LocalTime.of(15, 30)),
    H_1545(LocalTime.of(15, 45)),

    H_1600(LocalTime.of(16, 0)),
    H_1615(LocalTime.of(16, 15)),
    H_1630(LocalTime.of(16, 30)),
    H_1645(LocalTime.of(16, 45)); // Último slot de inicio (La cita termina a las 17:00)


    private final LocalTime hora;

    HorariosDisponibles(LocalTime hora) {
        this.hora = hora;
    }

    public LocalTime getHora() {
        return hora;
    }
}