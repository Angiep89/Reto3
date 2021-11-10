/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AP_Reto3.Reto3;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import AP_Reto3.Reto3.Reportes.ContadorClientes;
import AP_Reto3.Reto3.Reportes.StatusReservas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Angiep
 */
@Service
public class ServiciosReservaciones {
     @Autowired
     /**
      * Se declara la variable de repositorio
      */
    private RepositorioReservaciones metodosCrud;

    /**
     *
     * @return
     */
    public List<Reservaciones> getAll(){
        return metodosCrud.getAll();
    }

    /**
     *
     * @param reservationId
     * @return
     */
    public Optional<Reservaciones> getReservation(int reservationId) {
        return metodosCrud.getReservation(reservationId);
    }

    /**
     *
     * @param reservation
     * @return
     */
    public Reservaciones save(Reservaciones reservation){
        if(reservation.getIdReservation()==null){
            return metodosCrud.save(reservation);
        }else{
            Optional<Reservaciones> e= metodosCrud.getReservation(reservation.getIdReservation());
            if(e.isEmpty()){
                return metodosCrud.save(reservation);
            }else{
                return reservation;
            }
        }
    }

    /**
     *
     * @param reservation
     * @return
     */
    public Reservaciones update(Reservaciones reservation){
        if(reservation.getIdReservation()!=null){
            Optional<Reservaciones> event= metodosCrud.getReservation(reservation.getIdReservation());
            if(!event.isEmpty()){

                if(reservation.getStartDate()!=null){
                    event.get().setStartDate(reservation.getStartDate());
                }
                if(reservation.getDevolutionDate()!=null){
                    event.get().setDevolutionDate(reservation.getDevolutionDate());
                }
                if(reservation.getStatus()!=null){
                    event.get().setStatus(reservation.getStatus());
                }
                metodosCrud.save(event.get());
                return event.get();
            }else{
                return reservation;
            }
        }else{
            return reservation;
        }
    }

    /**
     *
     * @param reservationId
     * @return
     */
    public boolean deleteReservation(int reservationId) {
        Boolean aBoolean = getReservation(reservationId).map(reservation -> {
            metodosCrud.delete(reservation);
            return true;
        }).orElse(false);
        return aBoolean;
    }

    public StatusReservas getReservacion(){
        List<Reservaciones>completed= metodosCrud.ReservacionStatus("completed");
        List<Reservaciones>cancelled = metodosCrud.ReservacionStatus("cancelled");
                return new StatusReservas(completed.size(), cancelled.size());

    }

    public List<Reservaciones> reporteTiempoServicio (String datoA, String datoB){
        SimpleDateFormat parser = new SimpleDateFormat ("yyyy-MM-dd");

        Date datoUno = new Date();
        Date datoDos = new Date();

        try{
            datoUno = parser.parse(datoA);
            datoDos = parser.parse(datoB);
        }catch(ParseException evt){
            evt.printStackTrace();
        }if(datoUno.before(datoDos)){
            return metodosCrud.ReservacionTiempoRepositorio(datoUno, datoDos);
        }else{
            return new ArrayList<>();

        }
    }

    public List<ContadorClientes> servicioTopClientes(){
        return metodosCrud.getTopClientes();
    }

}


