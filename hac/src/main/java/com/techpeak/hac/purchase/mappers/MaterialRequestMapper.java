package com.techpeak.hac.purchase.mappers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.techpeak.hac.core.dtos.UserDtoShort;
import com.techpeak.hac.core.dtos.UserHistoryResponse;
import com.techpeak.hac.inventory.dtos.StoreResponseShort;
import com.techpeak.hac.purchase.dtos.MaterialRequestLineWithStockDto;
import com.techpeak.hac.purchase.dtos.MaterialRequestResponse;
import jakarta.persistence.Tuple;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

public class MaterialRequestMapper {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private MaterialRequestMapper(){}

    public static MaterialRequestResponse mapToMaterialRequestResponse(Tuple tuple) throws IOException {
        MaterialRequestResponse response = new MaterialRequestResponse();
        response.setId(tuple.get("id", Long.class));
        response.setNumber(tuple.get("number", String.class));
        Date date = tuple.get("date", Date.class);
        response.setDate(date.toLocalDate() );
        response.setStatus(tuple.get("status", String.class));

        // Map store
        StoreResponseShort store = objectMapper.readValue(tuple.get("store", String.class), StoreResponseShort.class);
        response.setStore(store);


        response.setInternalRef(tuple.get("internalRef", Long.class));
        response.setCurrentPhase(tuple.get("currentPhase", String.class));

        // Map userDto
        UserDtoShort userDto = objectMapper.readValue(tuple.get("userDto", String.class), UserDtoShort.class);
        response.setUserDto(userDto);

        // Map lines
        JsonNode linesN = objectMapper.readTree(tuple.get("lines", String.class));
        ObjectReader reader = objectMapper.readerFor(new TypeReference<List<MaterialRequestLineWithStockDto>>() {});
        List<MaterialRequestLineWithStockDto> lines = reader.readValue(linesN);
        response.setLines(lines);

        // Map history
        objectMapper.registerModule(new JavaTimeModule());

        JsonNode historyN = objectMapper.readTree(tuple.get("history", String.class));
        ObjectReader readerH = objectMapper.readerFor(new TypeReference<List<UserHistoryResponse>>() {});
        List<UserHistoryResponse> history = readerH.readValue(historyN);
        response.setHistory(history);
        return response;
    }
}

