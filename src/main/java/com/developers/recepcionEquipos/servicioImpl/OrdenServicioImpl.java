package com.developers.recepcionEquipos.servicioImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.developers.recepcionEquipos.entidad.Cliente;
import com.developers.recepcionEquipos.entidad.Orden;
import com.developers.recepcionEquipos.repositorio.OrdenRepositorio;
import com.developers.recepcionEquipos.servicio.OrdenServicio;

@Service
public class OrdenServicioImpl implements OrdenServicio {

    @Autowired
    private OrdenRepositorio ordenRepositorio;

    @Override
    public Orden save(Orden orden) {
        return ordenRepositorio.save(orden);
    }

    @Override
    public List<Orden> findAll() {
        return ordenRepositorio.findAll();
    }

    @Override
    public void Delete(Integer IdOrden) {
        ordenRepositorio.deleteById(IdOrden);
    }

    @Override
    public Optional<Orden> get(Integer IdOrden) {
        return ordenRepositorio.findById(IdOrden);
    }

    @Override
    public void update(Orden orden) {
        ordenRepositorio.save(orden);
    }

    @Override
    public List<Orden> findByCliente(Cliente cliente) {
        return ordenRepositorio.findByCliente(cliente);
    }

    @Override
    public String generarNumeroOrden() {

        int numero = 0;
        String numeroConcatenado = "";

        List<Orden> ordenes = findAll();
        List<Integer> numeros = new ArrayList<Integer>();

        ordenes.stream().forEach(o -> numeros.add(Integer.parseInt(o.getNumero())));

        if (ordenes.isEmpty()) {
            numero = 1;
        } else {
            numero = numeros.stream().max(Integer::compare).get();
            numero++;
        }

        if (numero < 10) {
            numeroConcatenado = "000000000" + String.valueOf(numero);
        } else if (numero < 100) {
            numeroConcatenado = "00000000" + String.valueOf(numero);
        } else if (numero < 1000) {
            numeroConcatenado = "0000000" + String.valueOf(numero);
        } else if (numero < 10000) {
            numeroConcatenado = "000000" + String.valueOf(numero);
        } else if (numero < 100000) {
            numeroConcatenado = "00000" + String.valueOf(numero);
        } else if (numero < 1000000) {
            numeroConcatenado = "0000" + String.valueOf(numero);
        } else if (numero < 10000000) {
            numeroConcatenado = "000" + String.valueOf(numero);
        }

        return numeroConcatenado;
    }
}
