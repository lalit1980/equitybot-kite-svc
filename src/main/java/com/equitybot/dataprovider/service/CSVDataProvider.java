package com.equitybot.dataprovider.service;

import com.equitybot.common.model.TickDTO;
import com.equitybot.dataprovider.util.DataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;

@Service
public class CSVDataProvider {

    @Autowired
    private DataProviderService dataProviderService;

    public void serve(Long instrument, String inputFile) throws IOException, ParseException {
        try (Scanner scanner = new Scanner(new File(inputFile))) {
            while (scanner.hasNextLine()) {
                TickDTO tickDTO = DataMapper.mapInTick(scanner.nextLine(), instrument);
                dataProviderService.serve(tickDTO);
            }
        }
    }


}
