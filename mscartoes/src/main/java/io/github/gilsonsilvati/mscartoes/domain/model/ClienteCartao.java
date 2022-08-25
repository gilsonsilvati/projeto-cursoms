package io.github.gilsonsilvati.mscartoes.domain.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "cliente_cartao")
@Data
public class ClienteCartao implements Serializable {

    @Serial
    private static final long serialVersionUID = 2025116347539172826L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cpf;

    @ManyToOne
    @JoinColumn(name = "id_cartao", nullable = false)
    private Cartao cartao;

    @Column(nullable = false)
    private BigDecimal limite;
}
