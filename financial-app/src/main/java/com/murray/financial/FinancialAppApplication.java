package com.murray.financial;

import com.murray.financial.importer.CSVImporterService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class FinancialAppApplication {


    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(FinancialAppApplication.class, args);

        CSVImporterService csvImporterService = applicationContext.getBean(CSVImporterService.class);

        csvImporterService.checkAndImportFiles();

    }


}
