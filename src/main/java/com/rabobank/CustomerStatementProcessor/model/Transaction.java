package com.rabobank.CustomerStatementProcessor.model;

import lombok.*;
import org.apache.commons.math3.util.Precision;

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
public class Transaction {
    @XmlAttribute
    private int reference;
    @XmlElement(name="accountNumber")
    private String accountNumber;
    @XmlElement(name="startBalance")
    private double startBalance;
    @XmlElement(name="mutation")
    private double mutation;
    @XmlElement(name="description")
    private String description;
    @XmlElement(name="endBalance")
    private double endBalance;

    public boolean hasValidClosingAmount() {
        return Precision.equals(this.endBalance, this.startBalance + this.mutation);
    }
}
