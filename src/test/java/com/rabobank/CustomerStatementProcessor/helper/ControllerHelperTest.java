package com.rabobank.CustomerStatementProcessor.helper;

import com.rabobank.CustomerStatementProcessor.constants.ProcessorConstants;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class ControllerHelperTest {

    @InjectMocks
    ControllerHelper controllerHelper;

    private InputStream stream;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void whenCSVFileFormat_validate_thenSuccess() throws IOException {
        stream = controllerHelper.getClass().getClassLoader().getResourceAsStream("records.csv");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file","records.csv", ProcessorConstants.FILE_TYPE_CSV, stream);
        boolean isValidFileFormat = controllerHelper.validateFileFormat(mockMultipartFile);
        assertTrue(isValidFileFormat);
    }

    @Test
    public void whenXMLFileFormat_validate_thenSuccess() throws IOException {
        stream = controllerHelper.getClass().getClassLoader().getResourceAsStream("records.xml");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file","records.xml", ProcessorConstants.FILE_TYPE_XML, stream);
        boolean isValidFileFormat = controllerHelper.validateFileFormat(mockMultipartFile);
        assertTrue(isValidFileFormat);
    }

    @Test
    public void whenHTMLFileFormat_validate_thenSuccess() throws IOException {
        stream = controllerHelper.getClass().getClassLoader().getResourceAsStream("records.html");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file","records.hrml", "application/html", stream);
        boolean isValidFileFormat = controllerHelper.validateFileFormat(mockMultipartFile);
        assertFalse(isValidFileFormat);
    }
}