package org.beer.works.jms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HelloWorldMessage implements Serializable {

    static final long serialVersionUID = -5996800369351524527L;

    private UUID id;
    private String message;

}
