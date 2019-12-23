package com.rabobank.CustomerStatementProcessor.controller;

import com.rabobank.CustomerStatementProcessor.constants.ProcessorConstants;
import com.rabobank.CustomerStatementProcessor.helper.ControllerHelper;
import com.rabobank.CustomerStatementProcessor.model.ErrorTransaction;
import com.rabobank.CustomerStatementProcessor.model.StatementProcessorResponse;
import com.rabobank.CustomerStatementProcessor.service.FileProcessorService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
@Slf4j
public class FileProcessorController {

    @Autowired
    private ControllerHelper controllerHelper;

    @Autowired
    private FileProcessorService fileProcessorService;

    @ApiOperation(value = "Process Customer Transactions in File", notes = "Returns a report which will display both the transaction reference and description of each of the failed records.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Username", paramType = "Basic Authentication", value = "rabobank", required = true, dataType = "String"),
            @ApiImplicitParam(name = "Password", paramType = "Basic Authentication", value = "rabobank", required = true, dataType = "String")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 415, message = "Unsupported Media Type", reference = "If files are not uploaded"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not Found")})
    @PostMapping(value = "/processFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StatementProcessorResponse> processFile(@RequestParam(required = true) MultipartFile file) {
        log.info("Entering into processFile");

        if (!controllerHelper.validateFileFormat(file))
            return constructInvalidFileFormatResponse();

        List<ErrorTransaction> errorTransactions = fileProcessorService.processFileInformation(file);
        log.info("Exit into processFile");
        return constructSuccessFileFormatResponse(errorTransactions);
    }

    private ResponseEntity<StatementProcessorResponse> constructSuccessFileFormatResponse(List<ErrorTransaction> errorTransactions) {
        return ResponseEntity.ok().body(StatementProcessorResponse.builder()
                .status(ProcessorConstants.SUCCESS_FILE_FORMAT_CODE)
                .decription(ProcessorConstants.COMPLETED_FILE_FORMAT_MSG)
                .errorTransactions(errorTransactions)
                .build());
    }

    private ResponseEntity<StatementProcessorResponse> constructInvalidFileFormatResponse() {
        return ResponseEntity.ok().body(StatementProcessorResponse.builder()
                .status(ProcessorConstants.INVALID_FILE_FORMAT_CODE)
                .decription(ProcessorConstants.UNSUPORTED_FILE_FORMAT_MSG)
                .build());

    }
}
