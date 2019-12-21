package com.rabobank.CustomerStatementProcessor.model;

import lombok.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@XmlAccessorType(XmlAccessType.FIELD)
public class Record {
    @XmlAttribute
    private int reference;
    @XmlElement(name="accountNumber")
    private String accountNumber;
    @XmlElement(name="startBalance")
    private float startBalance;
    @XmlElement(name="mutation")
    private String mutation;
    @XmlElement(name="description")
    private String description;
    @XmlElement(name="endBalance")
    private float endBalance;
}
