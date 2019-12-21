package com.rabobank.CustomerStatementProcessor.model;

import lombok.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@XmlRootElement(name="records")
@XmlAccessorType(XmlAccessType.FIELD)
public class Records {

	@XmlElement(name="record")
	List<Record> record;
}
