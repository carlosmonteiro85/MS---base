package com.projeta.user.domain.model;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Embeddable
@Data
public class IdComposto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rev", referencedColumnName = "rev")
    private RevEntity rev;
}
