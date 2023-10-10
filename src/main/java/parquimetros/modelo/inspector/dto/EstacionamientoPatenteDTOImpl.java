package parquimetros.modelo.inspector.dto;

public class EstacionamientoPatenteDTOImpl implements EstacionamientoPatenteDTO {
    private String patente;
    private String calle;
    private String altura;
    private String fechaEntrada;
    private String horaEntrada;
    private String estado;

    public EstacionamientoPatenteDTOImpl(String patente, String calle, String altura,
                                      String fechaEntrada, String horaEntrada, String estado) {
        this.patente = patente;
        this.calle = calle;
        this.altura = altura;
        this.fechaEntrada = fechaEntrada;
        this.horaEntrada = horaEntrada;
        this.estado = estado;
    }

    @Override
    public String getPatente() {
        return patente;
    }

    @Override
    public String getCalle() {
        return calle;
    }

    @Override
    public String getAltura() {
        return altura;
    }

    @Override
    public String getFechaEntrada() {
        return fechaEntrada;
    }

    @Override
    public String getHoraEntrada() {
        return horaEntrada;
    }

    @Override
    public String getEstado() {
        return estado;
    }

}
