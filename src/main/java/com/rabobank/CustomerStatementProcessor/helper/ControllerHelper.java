package com.rabobank.CustomerStatementProcessor.helper;

import com.rabobank.CustomerStatementProcessor.constants.ProcessorConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
public class ControllerHelper {

    public boolean validateFileFormat(MultipartFile file) {
        if (file.getContentType().equalsIgnoreCase(ProcessorConstants.FILE_TYPE_CSV) || file.getContentType().equalsIgnoreCase(ProcessorConstants.FILE_TYPE_XML)) {
            return true;
        }
        return false;
    }
}
