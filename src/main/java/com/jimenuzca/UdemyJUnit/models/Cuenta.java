package com.jimenuzca.UdemyJUnit.models;

import com.jimenuzca.UdemyJUnit.exceptions.DineroInsuficienteException;

import java.math.BigDecimal;

public class Cuenta {
    private String persona;
    private BigDecimal saldo;
    private Banco banco;

    public Cuenta() {
    }

    public Cuenta(String persona, BigDecimal saldo) {
        this.persona = persona;
        this.saldo = saldo;
    }

    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    //como se restan en Bigdecimal
    public void debito(BigDecimal monto){
        //el BigDecimal es inmutable por eso
        //this.saldo = this.saldo.subtract(monto);
        BigDecimal nuevoSaldo = this.saldo.subtract(monto);
        if(nuevoSaldo.compareTo(BigDecimal.ZERO) < 0){
            throw new DineroInsuficienteException("Dinero insuficiente");
        }
        this.saldo = nuevoSaldo;
    }

    public void credito(BigDecimal monto) {

        this.saldo = this.saldo.add(monto);
    }

    //sobreescribimos el metodo equals para que compare por valor
    // ALT + Ins -> Override Methods
    // Comparamos sus valores: persona y saldo
    @Override
    public boolean equals(Object obj) {
        // validamos que nos manden un objeto de tipo cuenta
        if (!(obj instanceof Cuenta)) {
            return false;
        }
        Cuenta c = (Cuenta) obj;
        if (this.persona == null || this.saldo == null){
            return false;
        }
        return this.persona.equals(c.getPersona()) && this.saldo.equals(c.getSaldo());
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }
}
